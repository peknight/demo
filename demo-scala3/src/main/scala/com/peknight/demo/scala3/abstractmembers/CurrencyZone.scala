package com.peknight.demo.scala3.abstractmembers

abstract class CurrencyZone:
  type Currency <: AbstractCurrency
  def make(x: Long): Currency

  abstract class AbstractCurrency:

    val amount: Long
    def designation: String

    def + (that: Currency): Currency = make(this.amount + that.amount)

    def * (x: Double): Currency = make((this.amount * x).toLong)

    def - (that: Currency): Currency = make(this.amount - that.amount)

    def / (that: Double) = make((this.amount / that).toLong)

    def / (that: Currency) = this.amount.toDouble / that.amount

    def from(other: CurrencyZone#AbstractCurrency): Currency =
      make(math.round(
        other.amount.toDouble * Converter.exchangeRate(other.designation)(this.designation)
      ))

    private def decimals(n: Long): Int = if n == 1 then 0 else 1 + decimals(n / 10)

    override def toString: String =
      s"%.${decimals(CurrencyUnit.amount)}f $designation".format(amount.toDouble / CurrencyUnit.amount.toDouble)

  end AbstractCurrency

  val CurrencyUnit: Currency

end CurrencyZone

