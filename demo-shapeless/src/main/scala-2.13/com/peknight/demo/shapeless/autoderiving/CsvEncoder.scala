package com.peknight.demo.shapeless.autoderiving

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

trait CsvEncoder[A] {
  def encode(value: A): List[String]
}

object CsvEncoder {

  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc

  def instance[A](func: A => List[String]): CsvEncoder[A] = (value: A) => func(value)

  // implicit val booleanEncoder: CsvEncoder[Boolean] = (b: Boolean) => if (b) List("yes") else List("no")

  implicit val hnilEncoder: CsvEncoder[HNil] = instance(_ => Nil)

  implicit def hlistEncoder[H, T <: HList](implicit hEncoder: Lazy[CsvEncoder[H]], tEncoder: CsvEncoder[T])
  : CsvEncoder[H :: T] = instance {
    case h :: t => hEncoder.value.encode(h) ++ tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](implicit gen: Generic.Aux[A, R], enc: Lazy[CsvEncoder[R]]): CsvEncoder[A] =
    instance(a => enc.value.encode(gen.to(a)))

  implicit val cnilEncoder: CsvEncoder[CNil] = instance(_ => throw new Exception("Inconceivable!"))

  implicit def coproductEncoder[H, T <: Coproduct](implicit hEncoder: Lazy[CsvEncoder[H]], tEncoder: CsvEncoder[T])
  : CsvEncoder[H :+: T] = instance {
    case Inl(h) => hEncoder.value.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }

  implicit val stringEncoder: CsvEncoder[String] = instance(List(_))
  implicit val intEncoder: CsvEncoder[Int] = instance(num => List(num.toString))
  implicit val booleanEncoder: CsvEncoder[Boolean] = instance(b => if (b) List("yes") else List("no"))
  implicit val doubleEncoder: CsvEncoder[Double] = instance(d => List(d.toString))

  val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = implicitly

  // implicit val employeeEncoder: CsvEncoder[Employee] =
  //   (e: Employee) => List(e.name, e.number.toString, if (e.manager) "yes" else "no")

  // implicit val iceCreamEncoder: CsvEncoder[IceCream] = {
  //   //    (i: IceCream) => List(i.name, i.numCherries.toString, if (i.inCone) "yes" else "no")
  //   val gen = Generic[IceCream]
  //   val enc = CsvEncoder[gen.Repr]
  //   instance(iceCream => enc.encode(gen.to(iceCream)))
  // }

  // implicit def pairEncoder[A, B](implicit aEncoder: CsvEncoder[A], bEncoder: CsvEncoder[B]): CsvEncoder[(A, B)] =
  //   (pair: (A, B)) => {
  //     val (a, b) = pair
  //     aEncoder.encode(a) ++ bEncoder.encode(b)
  //   }

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")
}
