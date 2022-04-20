package com.peknight.demo.cats.monad

import cats.Eval

object EvalApp extends App:
  val x =
    println("Computing X")
    math.random()

  println("before print x")
  println(x)
  println(x)

  def y =
    println("Computing Y")
    math.random()

  println("before print y")
  println(y)
  println(y)

  lazy val z =
    println("Computing Z")
    math.random()

  println("before print z")
  println(z)
  println(z)

  val now = Eval.now(math.random() + 1000)
  val always = Eval.always(math.random() + 3000)
  val later = Eval.later(math.random() + 2000)
  println(now.value)
  println(now.value)
  println(always.value)
  println(always.value)
  println(later.value)
  println(later.value)

  val xEval = Eval.now {
    println("Computing X")
    math.random()
  }
  println("before print x")
  println(xEval.value)
  println(xEval.value)

  val yEval = Eval.always {
    println("Computing Y")
    math.random()
  }
  println("before print y")
  println(yEval.value)
  println(yEval.value)

  val zEval = Eval.later {
    println("Computing Z")
    math.random()
  }
  println("before print z")
  println(zEval.value)
  println(zEval.value)

  val greeting = Eval.always {
    println("Step 1")
    "Hello"
  } map { str => {
    println("Step 2")
    s"$str world"
  }}
  println("before print greeting")
  println(greeting.value)
  println(greeting.value)

  val ans =
    for
      a <- Eval.now { println("Calculating A"); 40 }
      b <- Eval.always { println("Calculating B"); 2 }
    yield
      println("Adding A and B")
      a + b

  println("before print ans")
  println(ans.value)
  println(ans.value)

  val saying = Eval
    .always { println("Step 1"); "The cat" }
    .map { str => println("Step 2"); s"$str sat on" }
    .memoize
    .map { str => println("Step 3"); s"$str the mat" }
  println("before print saying")
  println(saying.value)
  println(saying.value)

  def factorial(n: BigInt): Eval[BigInt] = if n == 1 then Eval.now(n) else Eval.defer(factorial(n - 1).map(_ * n))
  // println(factorial(50000).value) // 卧槽

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = as match
    case head :: tail => fn(head, foldRight(tail, acc)(fn))
    case Nil => acc

  def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = as match
    case head :: tail => Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
    case Nil => acc

  def foldRightWithEval[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc)) { (a, b) =>
      b.map(fn(a, _))
    }.value

  println(foldRightWithEval((1 to 100000).toList, 0L)(_ + _))
