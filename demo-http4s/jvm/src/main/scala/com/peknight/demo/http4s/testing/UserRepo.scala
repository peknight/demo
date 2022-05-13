package com.peknight.demo.http4s.testing

trait UserRepo[F[_]]:
  def find(userId: String): F[Option[User]]
