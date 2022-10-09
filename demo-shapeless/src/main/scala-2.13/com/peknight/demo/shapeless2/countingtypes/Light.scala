package com.peknight.demo.shapeless2.countingtypes

sealed trait Light

object Light {
  case object Red extends Light
  case object Amber extends Light
  case object Green extends Light
}