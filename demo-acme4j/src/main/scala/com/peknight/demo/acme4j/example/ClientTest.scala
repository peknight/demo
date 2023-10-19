package com.peknight.demo.acme4j.example

import cats.data.EitherT
import cats.effect.*
import cats.syntax.applicative.*
import cats.syntax.either.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.shredzone.acme4j.exception.AcmeException
import org.shredzone.acme4j.util.KeyPairUtils
import org.shredzone.acme4j.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.*

import java.io.{Closeable, File, FileReader, FileWriter}
import java.net.URI
import java.security.{KeyPair, Security}
import javax.swing.JOptionPane
import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.Try

object ClientTest extends IOApp:

  // File name to the User Key Pair
  private[this] val userKeyFile: String = "demo-acme4j/src/main/resources/user.key"
  // File name of the Domain Key Pair
  private[this] val domainKeyFile: String = "demo-acme4j/src/main/resources/domain.key"
  // File name of the CSR
  private[this] val domainCsrFile: String = "demo-acme4j/src/main/resources/domain.csr"
  // File name of the signed certificate
  private[this] val domainChainFile: String = "demo-acme4j/src/main/resources/domain-chain.crt"
  private[this] enum ChallengeType derives CanEqual:
    case HTTP, DNS
  end ChallengeType

  //Challenge type to be used
  private[this] val challengeType: ChallengeType = ChallengeType.HTTP

  // RSA key size of generated key pairs
  private[this] val keySize: Int = 2048

  private[this] def attempt[F[_] : Sync, A](a: => A): EitherT[F, Throwable, A] =
    EitherT(Sync[F].blocking(Try(a).toEither))

  private[this] def use[F[_]: Sync, A <: Closeable, B](acquire: => A)(f: A => EitherT[F, Throwable, B])
  : EitherT[F, Throwable, B] =
    Resource.make[[T] =>> EitherT[F, Throwable, T], A](attempt(acquire))(a => attempt(a.close())).use(f)

  /**
   * Generates a certificate for the given domains. Also takes care for the registration
   * process.
   *
   * @param domains
   * Domains to get a common certificate for
   */
  def fetchCertificate[F[_]: Sync: Logger](domains: List[String]): EitherT[F, Throwable, Unit] =
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
  private[this] def loadOrCreateUserKeyPair[F[_]: Sync]: EitherT[F, Throwable, KeyPair] =
    loadOrCreateKeyPair(userKeyFile)

  /**
   * Loads a domain key pair from {@link # DOMAIN_KEY_FILE}. If the file does not exist,
   * a new key pair is generated and saved.
   *
   * @return Domain {@link KeyPair}.
   */
  private[this] def loadOrCreateDomainKeyPair[F[_]: Sync]: EitherT[F, Throwable, KeyPair] =
    loadOrCreateKeyPair(domainKeyFile)

  private[this] def loadOrCreateKeyPair[F[_]: Sync](keyFile: String): EitherT[F, Throwable, KeyPair] =
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
  private[this] def findOrRegisterAccount[F[_]: Sync: Logger](session: Session, accountKey: KeyPair)
  : EitherT[F, Throwable, Account] =
    for
      // Ask the user to accept to TOS, if server provides us with a link
      tos <- attempt(session.getMetadata.getTermsOfService.toScala)
      _ <- tos match
        case Some(agreement) => acceptAgreement(agreement)
        case _ => EitherT(().asRight[Throwable].pure[F])
      account <- attempt(AccountBuilder().agreeToTermsOfService().useKeyPair(accountKey).create(session))
      _ <- EitherT(info"Registered a new user, URL: ${account.getLocation}".map(_.asRight))
    yield account

  /**
   * Authorize a domain. It will be associated with your account, so you will be able to
   * retrieve a signed certificate for the domain later.
   *
   * @param auth
   * {@link Authorization} to perform
   */
  private[this] def authorize[F[_]: Sync: Logger](auth: Authorization): EitherT[F, Throwable, Unit] =
    given CanEqual[Status, Status] = CanEqual.derived
    for
      domain <- attempt[F, String](auth.getIdentifier.getDomain)
      _ <- EitherT(info"Authorization for domain $domain".map(_.asRight))
      status <- attempt(auth.getStatus)
      _ <-
        if status == Status.VALID then EitherT(().asRight.pure)
        else
          challengeType match
            case ChallengeType.HTTP => ???
            case ChallengeType.DNS => ???
    yield ()




  /**
   * Presents the user a link to the Terms of Service, and asks for confirmation. If the
   * user denies confirmation, an exception is thrown.
   *
   * @param agreement
   * {@link URI} of the Terms of Service
   */
  private[this] def acceptAgreement[F[_]: Sync](agreement: URI): EitherT[F, Throwable, Unit] =
    for
      option <- attempt(JOptionPane.showConfirmDialog(null,
        s"Do you accept the Terms of Service?\n\n$agreement", "Accept ToS", JOptionPane.YES_NO_OPTION))
      _ <-
        if option == JOptionPane.NO_OPTION then
          EitherT(AcmeException("User did not accept Terms of Service").asLeft[Unit].pure[F])
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
