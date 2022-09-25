package com.peknight.demo.oauth2.domain

import cats.Show
import io.circe.{Decoder, Encoder}

enum AuthMethod(val value: String) derives CanEqual:
  // 客户端不在令牌端点上进行身份认证，可能是因为客户端不使用令牌端点，或者使用令牌端点但它是公开客户端
  case None extends AuthMethod("none")
  // OpenID Connect协议特有的一些字段用的是ClientSecretBasic
  case SecretBasic extends AuthMethod("secret_basic")
  // 客户端使用HTTP Basic发送其客户端密钥。如果不指定且已为客户端颁发了密钥，则默认为此值
  case ClientSecretBasic extends AuthMethod("client_secret_basic")
  // OpenID Connect协议特有的一些字段用的是ClientSecretPost
  case SecretPost extends AuthMethod("secret_post")
  // 客户端使用表单参数发送客户端密钥
  case ClientSecretPost extends AuthMethod("client_secret_post")
  // 客户端会创建一个用客户端密钥进行对称签名的JSON Web令牌（JWT）
  case ClientSecretJwt extends AuthMethod("client_secret_jwt")
  // 客户端会创建一个用客户端私钥进行非对称签名的JSON Web令牌（JWT）。客户端需要在授权服务器上注册自己的公钥
  case PrivateKeyJwt extends AuthMethod("private_key_jwt")

object AuthMethod:
  def fromString(authMethod: String): Option[AuthMethod] = AuthMethod.values.find(_.value == authMethod)

  given Encoder[AuthMethod] = Encoder.encodeString.contramap[AuthMethod](_.value)

  given Decoder[AuthMethod] =
    Decoder.decodeString.emap[AuthMethod](str => fromString(str).toRight(s"No such AuthMethod: $str"))

  given Show[AuthMethod] with
    def show(t: AuthMethod): String = t.value