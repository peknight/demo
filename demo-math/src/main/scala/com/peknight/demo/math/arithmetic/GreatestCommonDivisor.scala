package com.peknight.demo.math.arithmetic

import org.apache.commons.math3.util.ArithmeticUtils

/**
 * 最大公约数
 */
object GreatestCommonDivisor {
  def gcd(p: Int, q: Int): Int = {
    println(ArithmeticUtils.lcm(256, 91))
    ArithmeticUtils.gcd(p, q)
  }
}
