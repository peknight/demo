package com.peknight.demo.shapeless.generic.mapper

trait Mapper[F[_], A, B]:
  def to(a: A): F[B]
end Mapper

