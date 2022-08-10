package com.peknight.demo.oauth2

import cats.data.NonEmptyList
import cats.syntax.option.*
import com.peknight.demo.oauth2.domain.*
import org.http4s.syntax.literals.uri

package object constant:

  val accessTokenKey = "access_token"
  val clientIdKey = "client_id"
  val oauthStateKey = "oauth-state"
  val oauthAccessTokenCls = "oauth-access-token"
  val oauthAuthorizeCls = "oauth-authorize"
  val oauthFetchResourceCls = "oauth-fetch-resource"
  val oauthProtectedResourceCls = "oauth-protected-resource"
  val oauthScopeValueCls = "oauth-scope-value"
  val redirectUriKey = "redirect_uri"
  val refreshTokenKey = "refresh_token"
  val responseTypeKey = "response_type"
  val scopeKey = "scope"
  val stateKey = "state"
  val tokenTypeKey = "token_type"
  val userKey = "user"

  val authServer: AuthServerInfo = AuthServerInfo(
    uri"http://localhost:8001/authorize",
    uri"http://localhost:8001/token",
    uri"http://localhost:8001/revoke",
    uri"http://localhost:8001/register",
    uri"http://localhost:8001/userinfo",
    uri"http://localhost:8001/introspect"
  )

  val client: ClientInfo = ClientInfo(
    "oauth-client-1",
    "oauth-client-secret-1",
    Set("foo", "bar", "read", "write", "delete", "fruit", "veggies", "meats", "movies", "foods", "music"),
    NonEmptyList.one(uri"http://localhost:8000/callback")
  )

  val webClient: ClientInfo = ClientInfo(
    "oauth-web-client-1",
    "oauth-web-client-secret-1",
    Set("foo", "bar"),
    NonEmptyList.one(uri"http://localhost:8010/callback")
  )

  val protectedResource = uri"http://localhost:8002/resource"

  val wordApi = uri"http://localhost:8002/words"

  val produceApi = uri"http://localhost:8002/produce"

  val favoritesApi = uri"http://localhost:8002/favorites"

  val rsaKey: RsaKey = RsaKey("RS256", "AQAB", "p8eP5gL1H_H9UNzCuQS-vNRVz3NWxZTHYk1tG9VpkfFjWNKG3MFTNZJ1l5g_COMm2_2i_YhQNH8MJ_nQ4exKMXrWJB4tyVZohovUxfw-eLgu1XQ8oYcVYW8ym6Um-BkqwwWL6CXZ70X81YyIMrnsGTyTV6M8gBPun8g2L8KbDbXR1lDfOOWiZ2ss1CRLrmNM-GRp3Gj-ECG7_3Nx9n_s5to2ZtwJ1GS1maGjrSZ9GRAYLrHhndrL_8ie_9DS2T-ML7QNQtNkg2RvLv4f0dpjRYI23djxVtAylYK4oiT_uEMgSkc4dxwKwGuBxSO0g9JOobgfy0--FUHHYtRi0dOFZw", "RSA", "authserver")

  val resource: Resource = Resource("Protected Resource", "This data has been protected by OAuth 2.0")

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

  val clients: Seq[ClientInfo] = Seq(client, webClient)

  val userInfos: Map[String, UserInfo] = Map(
    "alice" -> UserInfo(
      "9XE3-JI34-00132A",
      "alice",
      "Alice",
      "alice.wonderland@example.com",
      true
    ),
    "bob" -> UserInfo(
      "1ZT5-OE63-57383B",
      "bob",
      "Bob",
      "bob.loblob@example.net",
      false
    ),
    "carol" -> UserInfo(
      "F5Q1-L6LGG-959FS",
      "carol",
      "Carol",
      "carol.lewis@example.net",
      true,
      "clewis".some,
      "user password!".some
    )
  )
