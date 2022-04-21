package com.peknight.demo.fpinscala.introduction

trait Payments:
  def charge(cc: CreditCard, amount: Double): Unit
