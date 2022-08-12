package com.peknight.demo.oauth2

import cats.effect.IO
import com.peknight.demo.oauth2.constant.*
import org.http4s.*
import org.http4s.client.dsl.io.*
import org.http4s.dsl.io.*
import org.http4s.headers.*

package object request:
  def resourceRequest(accessToken: String): Request[IO] =
    POST(protectedResourceApi, Headers(Authorization(Credentials.Token(AuthScheme.Bearer, accessToken))))
