package com.peknight.demo.catseffect.spawn

trait Server[F[_]]:
  def accept: F[Connection[F]]
