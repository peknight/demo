package com.peknight.demo.cats.mtl

import cats.MonadError
import cats.data.*
import cats.mtl.Stateful
import cats.syntax.flatMap.*
import cats.syntax.functor.*

object CatsMTLApp extends App:
  def checkStateOld: EitherT[[X] =>> StateT[List, Int, X], Exception, String] =
    for
      currentState <- EitherT.liftF(StateT.get[List, Int])
      result <-
        if currentState > 10 then EitherT.leftT[[X] =>> StateT[List, Int, X], String](new Exception("Too large"))
        else EitherT.rightT[[X] =>> StateT[List, Int, X], Exception]("All good")
    yield
      result

  def checkState[F[_]](using S: Stateful[F, Int], E: MonadError[F, Exception]): F[String] =
    for
      currentState <- S.get
      result <- if currentState > 10 then E.raiseError(new Exception("Too Large")) else E.pure("All good")
    yield result

  val materializedProgram = checkState[[X] =>> EitherT[[Y] =>> StateT[List, Int, Y], Exception, X]]
end CatsMTLApp
