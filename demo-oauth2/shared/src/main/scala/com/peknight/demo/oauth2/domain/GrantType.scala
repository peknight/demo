package com.peknight.demo.oauth2.domain

import cats.Show
import io.circe.{Decoder, Encoder}

enum GrantType(val value: String) derives CanEqual:
  // 授权码许可，客户端将资源拥有者引导到授权端点，获取授权码，然后将授权码发回至令牌端点。需要对应使用的`response_type`为"code"
  case AuthorizationCode extends GrantType("authorization_code")
  // 隐式许可，客户端将资源拥有者引导至授权端点，直接获取令牌。需要对应使用的`response_type`为"token"
  case Implicit extends GrantType("implicit")
  // 资源拥有者密码许可，客户端向资源拥有者索取他们的用户名和密码，用于在令牌端点上换取令牌
  case Password extends GrantType("password")
  // 客户端凭据许可，客户端使用自己的凭据获取令牌
  case ClientCredentials extends GrantType("client_credentials")
  // 刷新令牌许可，在资源拥有者不在场的情况下，客户端使用刷新令牌获取新的访问令牌
  case RefreshToken extends GrantType("refresh_token")
  // JWT断言许可，客户端通过出示带有特定声明的JWT来获取令牌
  case UrnIetfParamsOAuthGrantTypeJwtBearer extends GrantType("urn:ietf:params:oauth:grant-type:jwt-bearer")
  // SAML断言许可，客户端通过出示带有特定声明的SAML文档来获取令牌
  case UrnIetfParamsOAuthGrantTypeSaml2Bearer extends GrantType("urn:ietf:params:oauth:grant-type:saml2-bearer")

object GrantType:
  def fromString(grantType: String): Option[GrantType] = GrantType.values.find(_.value == grantType)

  given Encoder[GrantType] = Encoder.encodeString.contramap[GrantType](_.value)

  given Decoder[GrantType] =
    Decoder.decodeString.emap[GrantType](str => fromString(str).toRight(s"No such GrantType: $str"))

  given Show[GrantType] with
    def show(t: GrantType): String = t.value