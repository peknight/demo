package com.peknight.demo.shapeless.countingtypes

import shapeless3.deriving.K0

import scala.Tuple.Size
import scala.compiletime.{constValue, erasedValue}
import scala.deriving.Mirror

trait Random[A]:
  def get: A

object Random:
  given Random[Int] with
    def get: Int = scala.util.Random.nextInt(10)

  given Random[Char] with
    def get: Char = ('A'.toInt + scala.util.Random.nextInt(26)).toChar

  given Random[Boolean] with
    def get: Boolean = scala.util.Random.nextBoolean()

  def instance[A](get0: => A): Random[A] =
    new Random[A]:
      def get: A = get0
  end instance

  inline given randomSum[A](using inst: => K0.CoproductInstances[Random, A], mirror: Mirror.SumOf[A]): Random[A] =
    instance[A] {
      val length = constValue[Size[mirror.MirroredElemTypes]]
      val choose = scala.util.Random.nextInt(length)
      inst.inject(choose)([t] => (random: Random[t]) => random.get).asInstanceOf[A]
    }

  given randomProduct[A](using inst: => K0.ProductInstances[Random, A], mirror: Mirror.ProductOf[A]): Random[A] with
    def get: A =
      mirror.fromProduct(inst.unfold(EmptyTuple: Tuple) { [t] => (acc: Tuple, random: Random[t]) =>
        val value = random.get
        (acc :* value, Some(value))
      }._1)

  inline def derived[A](using gen: K0.Generic[A]): Random[A] = gen.derive(randomProduct, randomSum)
