package com.peknight.demo.scala3.enums

enum Literal[T]:
  case IntLit(value: Int) extends Literal[Int]
  case LongLit(value: Long) extends Literal[Long]
  case CharLit(value: Char) extends Literal[Char]
  case FloatLit(value: Float) extends Literal[Float]
  case DoubleLit(value: Double) extends Literal[Double]
  case BooleanLit(value: Boolean) extends Literal[Boolean]
  case StringLit(value: String) extends Literal[String]
  
object Literal:
  def valueOfLiteral[T](lit: Literal[T]): T =
    lit match
      case IntLit(n) => n
      case LongLit(m) => m
      case CharLit(c) => c
      case FloatLit(f) => f
      case DoubleLit(d) => d
      case BooleanLit(b) => b
      case StringLit(s) => s

