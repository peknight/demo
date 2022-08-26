package com.peknight.demo.shapeless.introduction

import com.peknight.demo.shapeless.autoderiving.CsvEncoder

case class Employee(name: String, number: Int, manager: Boolean) derives CsvEncoder
