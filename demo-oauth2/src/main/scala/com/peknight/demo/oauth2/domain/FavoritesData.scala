package com.peknight.demo.oauth2.domain

case class FavoritesData(movies: Seq[String], foods: Seq[String], music: Seq[String])
object FavoritesData:
  val empty: FavoritesData = FavoritesData(Seq.empty[String], Seq.empty[String], Seq.empty[String])
