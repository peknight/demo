package com.peknight.demo.cats.effect.spawn

trait Connection[F[_]]:
  def read: F[Array[Byte]]
  def write(bytes: Array[Byte]): F[Unit]
  def close: F[Unit]
