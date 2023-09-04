package com.peknight.demo

import _root_.doobie.*
import cats.effect.IO

package object doobie:

  val xa = transactorWithLogHandler(None)

  def transactorWithLogHandler(logHandler: Option[LogHandler[IO]]) = Transactor.fromDriverManager[IO]
    .apply(
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:world",
      user = "postgres",
      password = "doobie",
      logHandler = logHandler
    )

end doobie
