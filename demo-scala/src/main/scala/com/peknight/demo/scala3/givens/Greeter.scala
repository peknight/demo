package com.peknight.demo.scala3.givens

object Greeter:
  def greet(name: String)(using prompt: PreferredPrompt, drink: PreferredDrink) =
    println(s"Welcome, $name. The system is ready.")
    print("But while you work, ")
    println(s"why not enjoy a cup of ${drink.preference}?")
    println(prompt.preference)


