package com.peknight.demo.cats.stm.theory

case class Account(private var balance: Long):
  def get: Long = this.synchronized {
    balance
  }
  def modify(f: Long => Long) = this.synchronized {
    balance = f(balance)
  }
end Account
