package com.peknight.demo

import _root_.doobie.*
import _root_.doobie.implicits.*
import cats.effect.IO

package object doobie:

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "doobie"
  )

