package com.peknight.demo.fpinscala.streamingio

case class T[I, I2]():
  sealed trait f[X]:
    def get: Either[I => X, I2 => X]

  val L = new f[I]:
    def get = Left(identity)

  val R = new f[I2]:
    def get = Right(identity)

object T:
  def L[I, I2] = T[I, I2]().L
  def R[I, I2] = T[I, I2]().R

