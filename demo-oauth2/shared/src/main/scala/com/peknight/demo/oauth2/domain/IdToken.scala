package com.peknight.demo.oauth2.domain

import com.peknight.demo.oauth2.constant.*
import io.circe.Codec

/**
 * OpenID Connect中其它声明：
 * auth_time 用户在IdP上通过身份时的时间戳
 * acr 身份认证上下文的引用，用于表示用户在IdP（身份提供方）上执行的身份认证的整体分类类型
 * amr 身份认证方法的引用，用于表示用户在IdP使用的身份认证方法
 * azp 该令牌的授权获得方；如果包含此声明则必须为RP的客户端ID
 * at_hash 访问令牌的加密散列
 * c_hash 授权码的加密散列
 *
 * OpenID Connect客户端注册协议：https://openid.net/specs/openid-connect-registration-1_0.html
 * OpenID Connect使用授权码流程的客户端：https://openid.net/specs/openid-connect-basic-1_0.html
 * OpenID Connect隐式客户端：https://openid.net/specs/openid-connect-implicit-1_0.html
 */
case class IdToken(
                    content: Option[String] = Some("{}"),
                    // iss: 令牌颁发者。它表示该令牌是由谁创建的，在很多OAuth部署中会将它设为授权服务器的URL。该声明是一个字符串
                    // OpenID Connect: IdP（身份提供方）的URL
                    issuer: Option[String] = None,
                    // sub: 令牌的主体。它表示该令牌是关于谁的，在很多OAuth部署中会将它设为资源拥有者的唯一标识。
                    // 在大多数情况下，主体在同一个颁发者的范围内必须是唯一的。该声明是一个字符串
                    // OpenID Connect: 稳定且唯一的IdP上的用户标识。它的值通常是一个机器可读的字符串，而且不应该将它作为用户名
                    subject: Option[String] = None,
                    // aud: 令牌的受众。它表示令牌的接收者，在很多OAuth部署中，它包含受保护资源的URI或者能够接收该令牌的受保护资源。
                    // 该声明可以是一个字符串数组，如果只有一个值，也可以是一个不用数组包装的单个字符串
                    // OpenID Connect: 必须包含RP（依赖方）的客户端ID
                    audience: Option[Set[String]] = None,
                    // exp: 令牌的过期时间戳。它表示令牌将在何时过期，以便部署应用让令牌自行失效。
                    // 该声明是一个整数，表示自UNIX新纪元（即格林威治标准时间GMT，1970年1月1日零点）以来的秒数
                    // OpenID Connect: 所有的ID令牌都会过期，而且一般都会很快
                    expiration: Option[Long] = None,
                    // nbf: 令牌生效时的时间戳。它表示令牌从什么时候开始生效，以便部署应用可以在令牌生效之前颁发令牌。
                    // 该声明是一个整数，表示自UNIX新纪元（即格林威治标准时间GMT，1970年1月1日零点）以来的秒数
                    notBefore: Option[Long] = None,
                    // iat: 令牌颁发的时间戳。它表示令牌是何时被创建的，它通常是颁发者在生成令牌时的系统时间戳。
                    // 该声明是一个整数，表示自UNIX新纪元（即格林威治标准时间GMT，1970年1月1日零点）以来的秒数
                    issuedAt: Option[Long] = None,
                    // jti: 令牌的唯一标识符。该声明的值在令牌颁发者创建的每一个令牌中都是唯一的，为了防止冲突，它通常是一个密码学的随机值。
                    // 这个值相当于向结构化的令牌中加入了一个攻击者无法获得的随机熵组件，有利于防止令牌猜测攻击和重放攻击
                    jwtId: Option[String] = None,
                    // nonce: RP（依赖方）在请求身份认证时发送的字符串，与state参数类似，用于缓解重话攻击。如果RP发送了该声明则必须包含
                    nonce: Option[String] = None
                  )

object IdToken:

  given Codec[IdToken] =
    Codec.forProduct9(
      contentKey,
      issuerKey,
      subjectKey,
      audienceKey,
      expirationKey,
      notBeforeKey,
      issuedAtKey,
      jwtIdKey,
      nonceKey
    )((content: Option[String],
       issuer: Option[String],
       subject: Option[String],
       audience: Option[Set[String]],
       expiration: Option[Long],
       notBefore: Option[Long],
       issuedAt: Option[Long],
       jwtId: Option[String],
       nonce: Option[String]
     ) => IdToken(content, issuer, subject, audience, expiration, notBefore, issuedAt, jwtId, nonce)
    )(t => (t.content, t.issuer, t.subject, t.audience, t.expiration, t.notBefore, t.issuedAt, t.jwtId, t.nonce))
