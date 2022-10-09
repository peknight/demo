package com.peknight.demo.shapeless.introduction

import com.peknight.demo.shapeless.autoderiving.CsvEncoder

case class IceCream(name: String, numCherries: Int, inCone: Boolean) derives CsvEncoder
