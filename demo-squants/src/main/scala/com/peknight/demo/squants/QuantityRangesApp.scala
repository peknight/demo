package com.peknight.demo.squants

import squants.QuantityRange
import squants.energy.{Kilowatts, Megawatts, Power}
import squants.space.Length
import squants.space.LengthConversions.*
import squants.time.Time
import squants.time.TimeConversions.*

object QuantityRangesApp extends App:
  val load1: Power = Kilowatts(1000)
  val load2: Power = Kilowatts(5000)
  val range: QuantityRange[Power] = QuantityRange(load1, load2)
  println(s"range=$range")
  // upper is strictly greater than lower
  val distances: QuantityRange[Length] = QuantityRange(1.km, 5.km)
  println(s"distances=$distances")
  // 可以编译，但是会抛异常
  // QuantityRange(1.km, 1.km)
  // contains不包含上边界
  println(distances.contains(5.km))
  // includes包含上边界
  println(distances.includes(5.km))

  // Create a Seq of 10 sequential ranges starting with the original and each the same size as the original
  val rs1 = range * 10
  println(s"rs1=$rs1")
  val rs2 = range / 10
  // Create a Seq of 10 sequential ranges each 1/10th of the original size:
  println(s"rs2=$rs2")
  // Create a Seq of 10 sequential ranges each with a size of 400 kilowatts:
  val rs3 = range / Kilowatts(400)
  println(s"rs3=$rs3")
  val rs4 = range / Kilowatts(317)
  println(s"rs4=$rs4")

  // Subdivide range into 1-Megawatt "slices", and foreach over each of slices:
  range.foreach(Megawatts(1)) { r => println(s"lower = ${r.lower}, upper = ${r.upper}") }
  // Subdivide range into 10 slices and map over each slice
  println(range.map(10)(_.upper))

  // Subdivide range into 10 slices and fold over them, using 0 Megawatts as a starting value:
  println(range.foldLeft(10, Megawatts(0)) { (z, r) => z + r.upper })

  // 由于foreach、map、fold*的需要divisor参数，签名不与增强for循环兼容，所以先 *或/ 之后再用增加for循环即可
  val timeUppers: Seq[Time] =
    for
      interval <- (0.seconds to 1.seconds) * 60
    yield interval.upper
  println(s"timeUppers=$timeUppers")
