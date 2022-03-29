package com.peknight.demo.fpinscala.localeffects

import scala.collection.mutable

/**
 * ST stand for state thread, state transition, state token, or state tag.
 * ST不含写方法，而STRef含写方法，所以要用ST把STRef"隐藏"起来
 */
sealed trait ST[S, A] { self =>
  protected def run(s: S): (A, S)

  def map[B](f: A => B): ST[S, B] = new ST[S, B] {
    def run(s: S) = {
      val (a, s1) = self.run(s)
      (f(a), s1)
    }
  }

  def flatMap[B](f: A => ST[S, B]): ST[S, B] = new ST[S, B] {
    def run(s: S) = {
      val (a, s1) = self.run(s)
      f(a).run(s1)
    }
  }
}

object ST {
  def apply[S, A](a: => A) = {
    lazy val memo = a
    new ST[S, A] {
      def run(s: S) = (memo, s)
    }
  }

  def runST[A](st: RunnableST[A]): A = st.apply[Unit].run(())._1

  def noop[S] = ST[S, Unit](())

  sealed trait STRef[S, A] {
    protected var cell: A
    def read: ST[S, A] = ST(cell)
    def write(a: A): ST[S, Unit] = new ST[S, Unit] {
      def run(s: S) = {
        cell = a
        ((), s)
      }
    }
  }

  object STRef {
    def apply[S, A](a: A): ST[S, STRef[S, A]] = ST(new STRef[S, A] {
      var cell = a
    })
  }

  sealed abstract class STArray[S, A](implicit manifest: Manifest[A]) {
    protected def value: Array[A]
    def size: ST[S, Int] = ST(value.size)

    def write(i: Int, a: A): ST[S, Unit] = new ST[S, Unit] {
      def run(s: S) = {
        value(i) = a
        ((), s)
      }
    }

    def read(i: Int): ST[S, A] = ST(value(i))

    def freeze: ST[S, List[A]] = ST(value.toList)

    // Exercise 14.1
    def fill(xs: Map[Int, A]): ST[S, Unit] = xs.foldRight(noop[S]) {
      case ((index, value), st) => st.flatMap(_ => write(index, value))
    }
  }

  object STArray {
    def apply[S, A: Manifest](sz: Int, v: A): ST[S, STArray[S, A]] = ST(new STArray[S, A] {
      lazy val value = Array.fill(sz)(v)
    })

    def fromList[S, A: Manifest](xs: List[A]): ST[S, STArray[S, A]] = ST(new STArray[S, A] {
      lazy val value = xs.toArray
    })
  }

  sealed trait STMap[S, K, V] {
    protected def table: mutable.HashMap[K, V]
    def size: ST[S, Int] = ST(table.size)

    // Get the value under a key
    def apply(k: K): ST[S, V] = ST(table(k))

    // Get the value under a key, or None if the key does not exist
    def get(k: K): ST[S, Option[V]] = ST(table.get(k))

    // Add a value under a key
    def +=(kv: (K, V)): ST[S, Unit] = ST(table += kv)

    // Remove a key
    def -=(k: K): ST[S, Unit] = ST(table -= k)
  }

  object STMap {
    def empty[S, K, V]: ST[S, STMap[S, K, V]] = ST(new STMap[S, K, V] {
      val table = (mutable.HashMap.empty[K, V])
    })
    def fromMap[S, K, V](m: Map[K, V]): ST[S, STMap[S, K, V]] = ST(new STMap[S, K, V] {
      val table = (mutable.HashMap.newBuilder[K, V] ++= m).result
    })
  }
}