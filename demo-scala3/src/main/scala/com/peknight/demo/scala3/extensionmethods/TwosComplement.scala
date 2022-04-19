package com.peknight.demo.scala3.extensionmethods

/**
 * 二进制补码的整数类型
 */
trait TwosComplement[N]:
  def equalsMinValue(n: N): Boolean
  def absOf(n: N): N
  def negationOf(n: N): N

object TwosComplement:
  given tcOfByte: TwosComplement[Byte] with
    def equalsMinValue(n: Byte): Boolean = n == Byte.MinValue
    def absOf(n: Byte): Byte = n.abs
    def negationOf(n: Byte): Byte = (-n).toByte

  given tcOfShort: TwosComplement[Short] with
    def equalsMinValue(n: Short): Boolean = n == Short.MinValue
    def absOf(n: Short): Short = n.abs
    def negationOf(n: Short): Short = (-n).toShort

  given tcOfInt: TwosComplement[Int] with
    def equalsMinValue(n: Int): Boolean = n == Int.MinValue
    def absOf(n: Int): Int = n.abs
    def negationOf(n: Int): Int = -n

  given tcOfLong: TwosComplement[Long] with
    def equalsMinValue(n: Long): Boolean = n == Long.MinValue
    def absOf(n: Long): Long = n.abs
    def negationOf(n: Long): Long = -n
