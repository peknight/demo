package com.peknight.demo

import cats.Functor
import cats.data.OptionT
import cats.syntax.functor.*
import cats.syntax.option.*
import fs2.io.file.Path

package object oauth2:
  
  val databaseNoSqlPath =
    Path("/Users/pek/project/github/oauthinaction/oauth-in-action-code/exercises/ch-3-ex-2/database.nosql")
    
  extension [F[_]: Functor, A](fa: F[A])
    def optionT = OptionT(fa.map(_.some))


