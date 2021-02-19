package com.peknight.demo.math.arithmetic

import org.apache.commons.math3.util.ArithmeticUtils

/**
 * 最小公倍数
 */
object LeastCommonMultiple {
  def lcm(p: Int, q: Int): Int = {
    ArithmeticUtils.lcm(p, q)
  }
}
