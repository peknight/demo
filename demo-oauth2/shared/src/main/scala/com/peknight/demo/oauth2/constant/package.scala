package com.peknight.demo.oauth2

import cats.data.NonEmptyList
import cats.syntax.option.*
import com.peknight.demo.oauth2.domain.*
import org.http4s.syntax.literals.uri

package object constant:

  val accessTokenKey = "access_token"
  val clientIdKey = "client_id"
  val clientIdCreatedAtKey = "client_id_created_at"
  val clientNameKey = "client_name"
  val clientSecretExpiresAtKey = "client_secret_expires_at"
  val clientSecretKey ="client_secret"
  val clientUriKey = "client_uri"
  val grantTypesKey = "grant_types"
  val idTokenKey = "id_token"
  val logoUriKey = "logo_uri"
  val oauthStateKey = "oauth-state"
  val oauthAccessTokenCls = "oauth-access-token"
  val oauthAuthorizeCls = "oauth-authorize"
  val oauthFetchResourceCls = "oauth-fetch-resource"
  val oauthGreetingCls = "oauth-greeting"
  val oauthProtectedResourceCls = "oauth-protected-resource"
  val oauthScopeValueCls = "oauth-scope-value"
  val redirectUriKey = "redirect_uri"
  val redirectUrisKey = "redirect_uris"
  val registrationAccessTokenKey = "registration_access_token"
  val registrationClientUriKey = "registration_client_uri"
  val refreshTokenKey = "refresh_token"
  val responseTypeKey = "response_type"
  val responseTypesKey = "response_types"
  val scopeKey = "scope"
  val stateKey = "state"
  val tokenEndpointAuthMethodKey = "token_endpoint_auth_method"
  val tokenTypeKey = "token_type"
  val userKey = "user"

  val authServer: AuthServerInfo = AuthServerInfo(
    uri"https://local.peknight.com:8001/authorize",
    uri"https://local.peknight.com:8001/token",
    uri"https://local.peknight.com:8001/revoke",
    uri"https://local.peknight.com:8001/register",
    uri"https://local.peknight.com:8001/userinfo",
    uri"https://local.peknight.com:8001/introspect"
  )

  val client: ClientInfo = ClientInfo(
    "oauth-client-1",
    "oauth-client-secret-1".some,
    Set("foo", "bar", "read", "write", "delete", "fruit", "veggies", "meats", "movies", "foods", "music", "openid",
      "profile", "email", "phone", "address", "greeting"),
    NonEmptyList.one(uri"https://local.peknight.com:8000/callback")
  )

  val webClient: ClientInfo = ClientInfo(
    "oauth-web-client-1",
    "oauth-web-client-secret-1".some,
    Set("foo", "bar", "openid", "profile", "email", "address", "phone", "greeting"),
    NonEmptyList.one(uri"https://local.peknight.com:8010/callback")
  )

  val clients: Seq[ClientInfo] = Seq(client, webClient,
    ClientInfo(
      "oauth-client-2",
      "oauth-client-secret-1".some,
      Set("bar"),
      NonEmptyList.one(uri"https://local.peknight.com:8000/callback")
    ),
    ClientInfo(
      "native-client-1",
      "oauth-native-secret-1".some,
      Set("foo", "bar"),
      NonEmptyList.one(uri"com.oauthinaction.mynativeapp://")
    )
  )

  val clientIndex = uri"https://local.peknight.com:8000/"

  val authorizationServerAddr = "local.peknight.com:8001"

  val authorizationServerIndex = uri"https://local.peknight.com:8001/"

  val protectedResourceAddr = "local.peknight.com:8002"

  val protectedResourceIndex = uri"https://local.peknight.com:8002/"

  val protectedResourceApi = uri"https://local.peknight.com:8002/resource"

  val helloWorldApi = uri"https://local.peknight.com:8002/helloWorld"

  val wordApi = uri"https://local.peknight.com:8002/words"

  val produceApi = uri"https://local.peknight.com:8002/produce"

  val favoritesApi = uri"https://local.peknight.com:8002/favorites"

  val sharedTokenSecret = "shared token secret!"

  val rsaKey: RsaKey = RsaKey(
    "RS256",
    "ZXFizvaQ0RzWRbMExStaS_-yVnjtSQ9YslYQF1kkuIoTwFuiEQ2OywBfuyXhTvVQxIiJqPNnUyZR6kXAhyj__wS_Px1EH8zv7BHVt1N5TjJGlubt1dhAFCZQmgz0D-PfmATdf6KLL4HIijGrE8iYOPYIPF_FL8ddaxx5rsziRRnkRMX_fIHxuSQVCe401hSS3QBZOgwVdWEb1JuODT7KUk7xPpMTw5RYCeUoCYTRQ_KO8_NQMURi3GLvbgQGQgk7fmDcug3MwutmWbpe58GoSCkmExUS0U-KEkHtFiC8L6fN2jXh1whPeRCa9eoIK8nsIY05gnLKxXTn5-aPQzSy6Q",
    "AQAB",
    "p8eP5gL1H_H9UNzCuQS-vNRVz3NWxZTHYk1tG9VpkfFjWNKG3MFTNZJ1l5g_COMm2_2i_YhQNH8MJ_nQ4exKMXrWJB4tyVZohovUxfw-eLgu1XQ8oYcVYW8ym6Um-BkqwwWL6CXZ70X81YyIMrnsGTyTV6M8gBPun8g2L8KbDbXR1lDfOOWiZ2ss1CRLrmNM-GRp3Gj-ECG7_3Nx9n_s5to2ZtwJ1GS1maGjrSZ9GRAYLrHhndrL_8ie_9DS2T-ML7QNQtNkg2RvLv4f0dpjRYI23djxVtAylYK4oiT_uEMgSkc4dxwKwGuBxSO0g9JOobgfy0--FUHHYtRi0dOFZw",
    "RSA",
    "authserver"
  )

  val resource: Resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

  val protectedResource: ProtectedResource = ProtectedResource("protected-resource-1", "protected-resource-secret-1")

  val protectedResources: Seq[ProtectedResource] = Seq(protectedResource)

  val aliceFavorites: FavoritesData = FavoritesData(
    Seq("The Multidimensional Vector", "Space Fights", "Jewelry Boss"),
    Seq("bacon", "pizza", "bacon pizza"),
    Seq("techno", "industrial", "alternative")
  )

  val bobFavorites: FavoritesData = FavoritesData(
    Seq("An Unrequited Love", "Several Shades of Turquoise", "Think Of The Children"),
    Seq("bacon", "kale", "gravel"),
    Seq("baroque", "ukulele", "baroque ukulele")
  )

  val userInfos: Map[String, UserInfo] = Map(
    "alice" -> UserInfo(
      "9XE3-JI34-00132A".some,
      "alice".some,
      "Alice".some,
      "alice.wonderland@example.com".some,
      true.some,
      "alice".some,
      "password".some
    ),
    "bob" -> UserInfo(
      "1ZT5-OE63-57383B".some,
      "bob".some,
      "Bob".some,
      "bob.loblob@example.net".some,
      false.some,
      "bob".some,
      "this is my secret password".some
    ),
    "carol" -> UserInfo(
      "F5Q1-L6LGG-959FS".some,
      "carol".some,
      "Carol".some,
      "carol.lewis@example.net".some,
      true.some,
      "clewis".some,
      "user password!".some
    )
  )
