package com.peknight.demo.shapeless.generic.mapper

trait BiMapper[F[_], A, B] extends Mapper[F, A, B]:
  def from(b: B): F[A]