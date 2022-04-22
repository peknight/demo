package com.peknight.demo.scala3.typeclasses

import scala.util.CommandLineParser.FromString

enum Mood derives CanEqual:
  case Surprised, Angry, Neutral

object Mood:

  val errmsg = "Please enter a word, a positive integer count, and \na mood (one of 'angry', 'surprised', or 'neutral')"

  given moodFromString: FromString[Mood] with
    def fromString(s: String): Mood =
      s.trim.toLowerCase match
        case "angry" => Mood.Angry
        case "surprised" => Mood.Surprised
        case "neutral" => Mood.Neutral
        case _ => throw new IllegalArgumentException(errmsg)
