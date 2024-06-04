package com.peknight.demo.security.otp

import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.digest.{HmacAlgorithms, HmacUtils}

import java.security.GeneralSecurityException
import scala.concurrent.duration.*

object GoogleAuthenticator {
  private val interval = 30.seconds

  def generateTotp(encoded: String, currentTimeMillis: Long): Either[GeneralSecurityException, String] =
    val mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_1, new Base32().decode(encoded))
    val passcodeGenerator = OneTimePassword(mac)
    passcodeGenerator.generateOtp(currentTimeMillis / interval.toMillis)
}
