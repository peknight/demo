package com.peknight.demo.oauth2.domain

case class ProduceData(fruit: Seq[String], veggies: Seq[String], meats: Seq[String])
object ProduceData:
  val empty: ProduceData = ProduceData(Seq.empty[String], Seq.empty[String], Seq.empty[String])