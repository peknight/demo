package com.peknight.demo.scala3.typeclasses

import com.peknight.demo.scala3.typeclasses.Mood.errmsg

object RepeatApp:

  @main def repeat(word: String, count: Int, mood: Mood) =
    val msg =
      if count > 0 then
        val words = List.fill(count)(word.trim)
        val punc =
          mood match
            case Mood.Angry => "!"
            case Mood.Surprised => "?"
            case Mood.Neutral => ""
        val sep = punc + " "
        words.mkString(sep) + punc
      else errmsg
    println(msg)

