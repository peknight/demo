package com.peknight.demo.shapeless.countingtypes

sealed trait Light

object Light {
  case object Red extends Light
  case object Amber extends Light
  case object Green extends Light
}