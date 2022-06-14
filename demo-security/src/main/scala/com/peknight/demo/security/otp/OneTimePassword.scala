package com.peknight.demo.security.otp

import com.peknight.demo.security.otp.OneTimePassword.*

import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Mac
import scala.annotation.tailrec

class OneTimePassword(private[this] val signer: Signer, private[this] val codeLength: Int):
  assert(0 <= codeLength && codeLength <= MAX_PASSCODE_LENGTH,
    s"PassCodeLength must be between 1 and ${MAX_PASSCODE_LENGTH} digits.")

  def generateOtp(state: Long, challenge: Option[Seq[Byte]] = None)
  : Either[GeneralSecurityException, String] =
    val value = challengeToBytes(state, challenge)
    signer(value).map(hash => truncate(hash, codeLength))

  def verifyOtp(oneTimePassword: String, state: Long, challenge: Option[Seq[Byte]] = None)
  : Either[GeneralSecurityException, Boolean] =
    generateOtp(state, challenge).map(_ == oneTimePassword)

  def verifyTimeoutCode(timeoutCode: String, currentInterval: Long,
                        pastIntervals: Int = ADJACENT_INTERVALS,
                        futureIntervals: Int = ADJACENT_INTERVALS): Either[GeneralSecurityException, Boolean] =
    val startInterval = currentInterval + Math.max(pastIntervals, 0)
    val endInterval = currentInterval - Math.max(futureIntervals, 0)
    @tailrec
    def go(interval: Long): Either[GeneralSecurityException, Boolean] =
      if interval >= endInterval then
        verifyOtp(timeoutCode, interval) match
          case r @ Right(true) => r
          case l @ Left(_) => l
          case _ => go(interval - 1)
      else Right(false)
    go(startInterval)
  end verifyTimeoutCode

  private[this] def challengeToBytes(state: Long, challenge: Option[Seq[Byte]] = None): Seq[Byte] =
    challenge.map {
      challengeBytes => {
        val challengeLength = challengeBytes.length
        ByteBuffer.allocate(8 + challengeLength)
          .putLong(state)
          .put(challengeBytes.toArray, 0, challengeLength)
          .array().toSeq
      }
    } getOrElse {
      ByteBuffer.allocate(8).putLong(state).array().toSeq
    }
  end challengeToBytes

  private[this] def truncate(hash: Seq[Byte], codeLength: Int): String =
    val offset = hash(hash.length - 1) & 0xF
    val truncatedHash = hashToInt(hash, offset) & 0x7FFFFFFF
    val pinValue = truncatedHash % DIGITS_POWER(codeLength)
    padOutput(pinValue, codeLength)

  private[this] def hashToInt(hash: Seq[Byte], offset: Int): Int =
    ((hash(offset) & 0xFF) << 24) |
      ((hash(offset + 1) & 0xFF) << 16) |
      ((hash(offset + 2) & 0xFF) << 8) |
      (hash(offset + 3) & 0xFF)

  private[this] def padOutput(value: Int, codeLength: Int): String =
    val str = value.toString
    val padLength = codeLength - str.length
    if padLength > 0 then "0".repeat(padLength) + str
    else str

end OneTimePassword

object OneTimePassword:

  type Signer = Seq[Byte] => Either[GeneralSecurityException, Seq[Byte]]

  private val MAX_PASSCODE_LENGTH: Int = 9
  private val PASS_CODE_LENGTH: Int = 6
  private val ADJACENT_INTERVALS: Int = 1

  /**
   * (1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000)
   */
  private val DIGITS_POWER: IndexedSeq[Int] = (1 to 9).scan(1)((x, _) => x * 10)

  def apply(mac: Mac): OneTimePassword = apply(mac, PASS_CODE_LENGTH)

  def apply(mac: Mac, passCodeLength: Int): OneTimePassword =
    apply(data => {
      try Right(mac.doFinal(data.toArray).toSeq)
      catch
        case gse: GeneralSecurityException => Left(gse)
        case e: Exception => Left(new GeneralSecurityException(e))
    }, passCodeLength)

  def apply(signer: Signer): OneTimePassword = apply(signer, PASS_CODE_LENGTH)

  def apply(signer: Signer, passCodeLength: Int): OneTimePassword =
    new OneTimePassword(signer, passCodeLength)

end OneTimePassword
