package com.peknight.demo.fpinscala.introduction

class Cafe:

  def buyCoffeeV1(cc: CreditCard): Coffee =
    val cup = new Coffee()
    cc.charge(cup.price)
    cup

  def buyCoffeeV2(cc: CreditCard, p: Payments): Coffee =
    val cup = new Coffee()
    p.charge(cc, cup.price)
    cup

  def buyCoffeeV3(cc: CreditCard): (Coffee, Charge) =
    val cup = new Coffee()
    (cup, Charge(cc, cup.price))

  def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) =
    val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffeeV3(cc))
    val (coffees, charges) = purchases.unzip
    (coffees, charges.reduce(_ combine _))

  def coalesce(charges: List[Charge]): List[Charge] =
    charges.groupBy(_.cc).values.map(_.reduce(_ combine _)).toList

end Cafe
