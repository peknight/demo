package com.peknight.demo.cats.effect.spawn

trait Server[F[_]]:
  def accept: F[Connection[F]]
