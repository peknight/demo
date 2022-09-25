package com.peknight.demo.oauth2.domain

import cats.data.NonEmptyList
import cats.effect.Concurrent
import com.peknight.demo.oauth2.constant.*
import com.peknight.demo.oauth2.domain
import io.circe.Codec
import org.http4s.circe.*
import org.http4s.{EntityDecoder, Uri}

/**
 * 其它未列举字段：
 * contacts: 客户端负责人员的联系方式列表。通常是电子邮箱地址，但也可能是电话号码，即时通信地址或者其他联系方式
 * tos_uri: 一个可读页面的URI，该页面列出了客户端服务条款。这些条款描述了资源拥有者对客户端授权时要接受的契约关系
 * policy_uri: 一个可读页面的URI，该页面包含客户端的隐私策略。譔策略描述了部署客户端的机构如何搜集、使用、保留以及公开资源拥有者的个人数据，包括通过授权API获取的数据
 * jwks_uri: 一个指向JSON Web密钥集合的URI，该密钥集合包含些客户端的公钥，可被授权服务器访问。该字段不能与jwks一起使用。优先使用`jwks_uri`字段
 * software_id: 客户端软件的唯一标识符。该标识符在同一个客户端软件的所有实例上都是相同的
 * software_version: `software_id`字段所标识的客户端软件的版本标识。版本字符串对授权服务器是不透明的，也不会假设它具有特定格式
 */
case class ClientMetadata(
                           // 客户端在令牌端点上进行身份认证的方式
                           tokenEndpointAuthMethod: AuthMethod,
                           // 客户端获取令牌所使用的许可类型。该字段使用的值与令牌端点上`grant_type`参数使用的值相同
                           grantTypes: List[GrantType],
                           // 客户端使用的授权端点响应类型。该字段使用的值与response_type`参数使用的值相同
                           responseTypes: List[ResponseType],
                           // 一个URI字符串数组，在基于重定向的OAuth许可类型中使用，比如`authorization_code`和`implicit`
                           redirectUris: NonEmptyList[Uri],
                           // 可读的客户端显示名称
                           clientName: Option[String],
                           // 指向客户端主页面的URI
                           clientUri: Option[Uri],
                           // 客户端图形标志的URI。授权服务器可以使用该URI向用户展示客户端的标志
                           // 但需要注意的是，获取图片URI资源可能会给用户带来安全和隐私方面的问题
                           logoUri: Option[Uri],
                           // 客户端请求令牌时所有可用的权限范围。它的值是以空格分隔的字符串，与OAuth协议中的同名字段一样
                           scope: Option[Set[String]]
                         ):
  def toClientInfo(clientId: String, clientSecret: Option[String], clientIdCreatedAt: Long,
                   clientSecretExpiresAt: Long, registrationAccessToken: String,
                   registrationClientUri: Uri): ClientInfo =
    ClientInfo(clientId, clientSecret, scope.getOrElse(Set.empty[String]), redirectUris, clientName, clientUri, logoUri,
      Some(tokenEndpointAuthMethod), Some(grantTypes), Some(responseTypes), Some(clientIdCreatedAt),
      Some(clientSecretExpiresAt), Some(registrationAccessToken), Some(registrationClientUri))

  def updateClientInfo(client: ClientInfo): ClientInfo =
    ClientInfo(client.id, client.secret, scope.getOrElse(client.scope), redirectUris, clientName.orElse(client.name),
      clientUri.orElse(client.uri), logoUri.orElse(client.logoUri), Some(tokenEndpointAuthMethod), Some(grantTypes),
      Some(responseTypes), client.clientIdCreatedAt, client.clientSecretExpiresAt, client.registrationAccessToken,
      client.registrationClientUri)

end ClientMetadata

object ClientMetadata:
  given Codec[ClientMetadata] =
    Codec.forProduct8(
      tokenEndpointAuthMethodKey,
      grantTypesKey,
      responseTypesKey,
      redirectUrisKey,
      clientNameKey,
      clientUriKey,
      logoUriKey,
      scopeKey
    )((tokenEndpointAuthMethod: AuthMethod,
       grantTypes: List[GrantType],
       responseTypes: List[ResponseType],
       redirectUris: NonEmptyList[Uri],
       clientName: Option[String],
       clientUri: Option[Uri], logoUri: Option[Uri], scope: Option[String]) =>
      ClientMetadata(tokenEndpointAuthMethod, grantTypes, responseTypes, redirectUris, clientName, clientUri, logoUri,
        scope.map(_.split("\\s++").toSet[String]))
    )(t => (t.tokenEndpointAuthMethod, t.grantTypes, t.responseTypes, t.redirectUris, t.clientName, t.clientUri,
      t.logoUri, t.scope.map(_.mkString(" "))))
  given [F[_]: Concurrent]: EntityDecoder[F, ClientMetadata] = jsonOf[F, ClientMetadata]
end ClientMetadata
