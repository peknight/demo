package com.peknight.demo.acme4j.example

import cats.data.EitherT
import cats.effect.*
import cats.syntax.applicative.*
import cats.syntax.either.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.traverse.*
import cats.{Functor, Monad}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.shredzone.acme4j.*
import org.shredzone.acme4j.challenge.{Challenge, Dns01Challenge, Http01Challenge}
import org.shredzone.acme4j.exception.AcmeException
import org.shredzone.acme4j.util.KeyPairUtils
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

import java.io.{Closeable, File, FileReader, FileWriter}
import java.net.URI
import java.security.{KeyPair, Security}
import javax.swing.JOptionPane
import scala.concurrent.duration.*
import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.Try

object ClientTest extends IOApp:

  // File name to the User Key Pair
  private val userKeyFile: String = "demo-acme4j/src/main/resources/user.key"
  // File name of the Domain Key Pair
  private val domainKeyFile: String = "demo-acme4j/src/main/resources/domain.key"
  // File name of the CSR
  private val domainCsrFile: String = "demo-acme4j/src/main/resources/domain.csr"
  // File name of the signed certificate
  private val domainChainFile: String = "demo-acme4j/src/main/resources/domain-chain.crt"
  private enum ChallengeType derives CanEqual:
    case HTTP, DNS
  end ChallengeType

  //Challenge type to be used
  private val challengeType: ChallengeType = ChallengeType.HTTP

  // RSA key size of generated key pairs
  private val keySize: Int = 2048

  private def attempt[F[_] : Sync, A](a: => A): EitherT[F, Throwable, A] =
    EitherT(Sync[F].blocking(Try(a).toEither))

  private def attemptEither[F[_] : Sync, A](a: => Either[Throwable, A]): EitherT[F, Throwable, A] =
    EitherT(Sync[F].blocking(Try(a).toEither.flatten))

  private def use[F[_]: Sync, A <: Closeable, B](acquire: => A)(f: A => EitherT[F, Throwable, B])
  : EitherT[F, Throwable, B] =
    Resource.fromAutoCloseable[[T] =>> EitherT[F, Throwable, T], A](attempt(acquire)).use(f)

  extension [F[_]: Functor, A] (fa: F[A])
    def lift: EitherT[F, Throwable, A] = EitherT(fa.map(_.asRight))
  end extension

  given CanEqual[Status, Status] = CanEqual.derived

  /**
   * Generates a certificate for the given domains. Also takes care for the registration
   * process.
   *
   * @param domains
   * Domains to get a common certificate for
   */
  def fetchCertificate[F[_]: Async: Logger](domains: List[String]): EitherT[F, Throwable, Unit] =
    for
      // Load the user key file. If there is no key file, create a new one.
      userKeyPair <- loadOrCreateUserKeyPair
      // Create a session for Let's Encrypt.
      // Use "acme://letsencrypt.org" for production server
      session <- attempt(Session("acme://letsencrypt.org/staging"))
      // Get the Account.
      // If there is no account yet, create a new one.
      acct <- findOrRegisterAccount(session, userKeyPair)
      // Load or create a key pair for the domains. This should not be the userKeyPair!
      domainKeyPair <- loadOrCreateDomainKeyPair
      // Order the certificate
      order <- attempt(acct.newOrder.domains(domains.asJavaCollection).create)
      // Perform all required authorizations
      authorizations <- attempt(order.getAuthorizations.asScala)
      _ <- authorizations.toList.traverse(authorize)
      // Order the certificate
      _ <- attempt(order.execute(domainKeyPair))
      // Wait for the order to complete
      _ <- waitForComplete(attempt(order.getStatus), attempt(order.getError.toScala.fold("unknown")(_.toString)),
        attempt(order.fetch()), "Order")
      // Get the certificate
      certificate <- attempt(order.getCertificate)
      _ <- info"Success! The certificate for domains $domains has been generated!".lift
      location <- attempt(certificate.getLocation)
      _ <- info"Certificate URL: $location".lift
      // Write a combined file containing the certificate and chain.
      _ <- use(FileWriter(domainChainFile))(fw => attempt(certificate.writeCertificate(fw)))
      // That's all! Configure your web server to use the DOMAIN_KEY_FILE and
      // DOMAIN_CHAIN_FILE for the requested domains.
    yield ()

  /**
   * Loads a user key pair from {@link # USER_KEY_FILE}. If the file does not exist, a
   * new key pair is generated and saved.
   * <p>
   * Keep this key pair in a safe place! In a production environment, you will not be
   * able to access your account again if you should lose the key pair.
   *
   * @return User's {@link KeyPair}.
   */
  private def loadOrCreateUserKeyPair[F[_]: Sync]: EitherT[F, Throwable, KeyPair] =
    loadOrCreateKeyPair(userKeyFile)

  /**
   * Loads a domain key pair from {@link # DOMAIN_KEY_FILE}. If the file does not exist,
   * a new key pair is generated and saved.
   *
   * @return Domain {@link KeyPair}.
   */
  private def loadOrCreateDomainKeyPair[F[_]: Sync]: EitherT[F, Throwable, KeyPair] =
    loadOrCreateKeyPair(domainKeyFile)

  private def loadOrCreateKeyPair[F[_]: Sync](keyFile: String): EitherT[F, Throwable, KeyPair] =
    for
      file <- attempt(File(keyFile))
      exists <- attempt(file.exists())
      keyPair <-
        if exists then
          use(FileReader(file))(fr => attempt(KeyPairUtils.readKeyPair(fr)))
        else
          for
            userKeyPair <- attempt(KeyPairUtils.createKeyPair())
            _ <- use(FileWriter(file))(fw => attempt(KeyPairUtils.writeKeyPair(userKeyPair, fw)))
          yield
            userKeyPair
    yield keyPair


  /**
   * Finds your {@link Account} at the ACME server. It will be found by your user's
   * public key. If your key is not known to the server yet, a new account will be
   * created.
   * <p>
   * This is a simple way of finding your {@link Account}. A better way is to get the
   * URL of your new account with {@link Account# getLocation ( )} and store it somewhere.
   * If you need to get access to your account later, reconnect to it via {@link
 * Session#login(URL, KeyPair)} by using the stored location.
   *
   * @param session
   * {@link Session} to bind with
   * @return {@link Account}
   */
  private def findOrRegisterAccount[F[_]: Sync: Logger](session: Session, accountKey: KeyPair)
  : EitherT[F, Throwable, Account] =
    for
      // Ask the user to accept to TOS, if server provides us with a link
      tos <- attempt(session.getMetadata.getTermsOfService.toScala)
      _ <- tos match
        case Some(agreement) => acceptAgreement(agreement)
        case _ => EitherT.pure(())
      account <- attempt(AccountBuilder().agreeToTermsOfService.useKeyPair(accountKey).create(session))
      _ <- info"Registered a new user, URL: ${account.getLocation}".lift
    yield account

  /**
   * Authorize a domain. It will be associated with your account, so you will be able to
   * retrieve a signed certificate for the domain later.
   *
   * @param auth
   * {@link Authorization} to perform
   */
  private def authorize[F[_]: Async: Logger](auth: Authorization): EitherT[F, Throwable, Unit] =
    attempt[F, String](auth.getIdentifier.getDomain)
      .flatMap(domain => info"Authorization for domain $domain".lift)
      .flatMap(_ => attempt(auth.getStatus))
      .flatMap {
        // The authorization is already valid. No need to process a challenge.
        case Status.VALID => EitherT.pure(())
        case _ =>
          // Find the desired challenge and prepare it.
          val challengeT = challengeType match
            case ChallengeType.HTTP => httpChallenge(auth)
            case ChallengeType.DNS => dnsChallenge(auth)
            // case _ => EitherT(AcmeException("No challenge found").asLeft[Challenge].pure[F])
          challengeT.flatMap { challenge =>
            val statusT = attempt(challenge.getStatus)
            statusT.flatMap {
              // If the challenge is already verified, there's no need to execute it again.
              case Status.VALID => EitherT.pure(())
              // Now trigger the challenge.
              // Poll for the challenge to complete
              case _ => attempt(challenge.trigger()).flatMap(_ => waitForComplete(statusT,
                attempt(challenge.getError.toScala.fold("unknown")(_.toString)),
                attempt(challenge.fetch()),
                "Challenge"
              )).flatMap(_ => statusT).flatMap {
                // All reattempts are used up and there is still no valid authorization?
                case status if status != Status.VALID =>
                  attempt(auth.getIdentifier.getDomain).flatMap(domain => EitherT(
                    AcmeException(s"Failed to pass the challenge for domain $domain, ... Giving up.")
                      .asLeft[Unit].pure[F]
                  ))
                case _ =>
                  info"Challenge has been completed. Remember to remove the validation resource.".lift.flatMap(_ =>
                    completeChallenge("Challenge has been completed.\nYou can remove the resource again now.")
                  )
              }
            }
          }
      }

  private def waitForComplete[F[_]: Async: Logger](statusT: EitherT[F, Throwable, Status],
                                                         errorT: EitherT[F, Throwable, String],
                                                         updateT: EitherT[F, Throwable, Unit], label: String)
  : EitherT[F, Throwable, Unit] =
    EitherT(Monad[[T] =>> EitherT[F, Throwable, T]].tailRecM[Int, Unit](10) { attempts =>
      statusT.flatMap {
        case Status.VALID if attempts <= 0 => EitherT.pure(().asRight[Int])
        case _ => statusT.flatMap {
          // Did the authorization fail?
          case Status.INVALID => EitherT(errorT.flatMap(error => error"$label has failed, reason: $error".lift).value
            .as(AcmeException(s"$label failed...Giving up.").asLeft[Either[Int, Unit]]))
          // Wait for a few seconds
          // Then update the status
          case _ => Async[F].sleep(3.seconds).lift.flatMap(_ => updateT).as((attempts - 1).asLeft[Unit])
        }
      }
    }.value.flatMap {
      case Left(e: InterruptedException) => Logger[F].error(e)("interrupted").map(_.asRight[Throwable])
      case either => Async[F].pure(either)
    })

  /**
   * Prepares a HTTP challenge.
   * <p>
   * The verification of this challenge expects a file with a certain content to be
   * reachable at a given path under the domain to be tested.
   * <p>
   * This example outputs instructions that need to be executed manually. In a
   * production environment, you would rather generate this file automatically, or maybe
   * use a servlet that returns {@link Http01Challenge# getAuthorization ( )}.
   *
   * @param auth
   * {@link Authorization} to find the challenge in
   * @return {@link Challenge} to verify
   */
  private def httpChallenge[F[_]: Sync: Logger](auth: Authorization): EitherT[F, Throwable, Challenge] =
    for
      challenge <- attemptEither(auth.findChallenge(classOf[Http01Challenge]).toScala
        .toRight[Throwable](AcmeException(s"Found no ${Http01Challenge.TYPE} challenge, don't know what to do...")))
      _ <- info"Please create a file in your web server's base directory.".lift
      domain <- attempt(auth.getIdentifier.getDomain)
      token <- attempt(challenge.getToken)
      _ <- info"It must be reachable at: http://$domain/.well-known/acme-challenge/$token".lift
      _ <- info"File name: $token".lift
      authorization <- attempt(challenge.getAuthorization)
      _ <- info"Content: $authorization".lift
      _ <- info"The file must not contain any leading or trailing whitespaces or line breaks!".lift
      _ <- info"If you're ready, dismiss the dialog...".lift
      message =
        s"""
          |Please create a file in your web server's base directory.\n
          |http://$domain/.well-known/acme-challenge/$token\n
          |Content\n
          |$authorization
        """.stripMargin
      _ <- acceptChallenge(message)
    yield challenge

  /**
   * Prepares a DNS challenge.
   * <p>
   * The verification of this challenge expects a TXT record with a certain content.
   * <p>
   * This example outputs instructions that need to be executed manually. In a
   * production environment, you would rather configure your DNS automatically.
   *
   * @param auth
   * {@link Authorization} to find the challenge in
   * @return {@link Challenge} to verify
   */
  private def dnsChallenge[F[_]: Sync: Logger](auth: Authorization): EitherT[F, Throwable, Challenge] =
    for
      challenge <- attemptEither(auth.findChallenge(Dns01Challenge.TYPE).toScala
        .map(_.asInstanceOf[Dns01Challenge])
        .toRight[Throwable](AcmeException(s"Found no ${Dns01Challenge.TYPE} challenge, don't know what to do...")))
      _ <- info"Please create a TXT record:".lift
      rrName <- attempt(Dns01Challenge.toRRName(auth.getIdentifier))
      digest <- attempt(challenge.getDigest)
      _ <- info"$rrName IN TXT $digest".lift
      info <- info"If you're ready, dismiss the dialog...".lift
      message =
        s"""
          |Please create a TXT record:\n
          |$rrName IN TXT $digest
        """.stripMargin
      _ <- acceptChallenge(message)
    yield challenge

  private def acceptChallenge[F[_]: Sync](message: String): EitherT[F, Throwable, Unit] =
    okCancelOption(message, "Prepare Challenge", "User cancelled the challenge")

  /**
   * Presents the instructions for removing the challenge validation, and waits for
   * dismissal.
   *
   * @param message
   * Instructions to be shown in the dialog
   */
  private def completeChallenge[F[_]: Sync](message: String): EitherT[F, Throwable, Unit] =
    attempt(JOptionPane.showMessageDialog(null, message, "Complete Challenge",
      JOptionPane.INFORMATION_MESSAGE))

  /**
   * Presents the user a link to the Terms of Service, and asks for confirmation. If the
   * user denies confirmation, an exception is thrown.
   *
   * @param agreement
   * {@link URI} of the Terms of Service
   */
  private def acceptAgreement[F[_]: Sync](agreement: URI): EitherT[F, Throwable, Unit] =
    okCancelOption(
      s"Do you accept the Terms of Service?\n\n$agreement",
      "Accept ToS",
      "User did not accept Terms of Service"
    )

  private def okCancelOption[F[_]: Sync](message: String, title: String, errorMessage: String)
  : EitherT[F, Throwable, Unit] =
    for
      option <- attempt(JOptionPane.showConfirmDialog(null, message, title,
        JOptionPane.YES_NO_OPTION))
      _ <-
        if option == JOptionPane.NO_OPTION then
          EitherT(AcmeException(errorMessage).asLeft[Unit].pure[F])
        else EitherT(().asRight[Throwable].pure[F])
    yield ()

  def run(args: List[String]): IO[ExitCode] =
    for
      logger <- Slf4jLogger.create[IO]
      given Logger[IO] = logger
      code <-
        if args.isEmpty then
          error"Usage: ClientTest <domain>...".as(ExitCode.Error)
        else
          for
            _ <- info"Starting up..."
            _ <- IO.blocking(Security.addProvider(BouncyCastleProvider()))
            either <- fetchCertificate[IO](args).value
            _ <- either match
              case Left(e) => logger.error(e)(s"Failed to get a certificate for domains $args")
              case _ => IO.unit
          yield ExitCode.Success
    yield code
end ClientTest
