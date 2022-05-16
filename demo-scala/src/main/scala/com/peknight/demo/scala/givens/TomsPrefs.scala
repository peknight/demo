package com.peknight.demo.scala.givens

object TomsPrefs:
  val favoriteColor = "blue"
  def favoriteFood = "steak"
  given prompt: PreferredPrompt = PreferredPrompt("enjoy> ")
  given drink: PreferredDrink = PreferredDrink("red wine")
  given prefPromptOrd: Ordering[PreferredPrompt] with 
    def compare(x: PreferredPrompt, y: PreferredPrompt) = x.preference.compareTo(y.preference)
    
  given prefDrinkOrd: Ordering[PreferredDrink] with
    def compare(x: PreferredDrink, y: PreferredDrink) = x.preference.compareTo(y.preference)
