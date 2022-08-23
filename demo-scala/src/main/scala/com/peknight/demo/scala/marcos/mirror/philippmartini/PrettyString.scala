package com.peknight.demo.scala.marcos.mirror.philippmartini

import scala.compiletime.{constValue, erasedValue, summonInline}
import scala.deriving.Mirror

trait PrettyString[A]:
  def prettyString(a: A): String

object PrettyString extends EasyDerive[PrettyString]:

  override def deriveCaseClass[A](productType: CaseClassType[A]): PrettyString[A] = (a: A) =>
    if productType.elements.isEmpty then productType.label else
      val prettyElements = productType.elements.map(p => s"${p.label}=${p.typeclass.prettyString(p.getValue(a))}")
      prettyElements.mkString(s"${productType.label}(", ", ", ")")

  override def deriveSealed[A](sumType: SealedType[A]): PrettyString[A] = (a: A) =>
    val elem = sumType.getElement(a)
    elem.typeclass.prettyString(elem.cast(a))

  given PrettyString[Int] with
    def prettyString(a: Int): String = a.toString

  given PrettyString[String] with
    def prettyString(a: String): String = s"\"$a\""

  given PrettyString[Long] with
    def prettyString(a: Long): String = a.toString

  given PrettyString[Double] with
    def prettyString(a: Double): String = a.toString

  given PrettyString[Boolean] with
    def prettyString(a: Boolean): String = a.toString

  // given PrettyString[User] with
  //   def prettyString(a: User): String =
  //     s"User(name=${summon[PrettyString[String]].prettyString(a.name)}, age=${summon[PrettyString[Int]].prettyString(a.age)})"

  def prettyPrintln[A: PrettyString](a: A) = println(summon[PrettyString[A]].prettyString(a))

  // inline given derived[A](using m: Mirror.Of[A]): PrettyString[A] =
  //   inline m match
  //     case s: Mirror.SumOf[A] => derivePrettyStringSealedTrait(using s)
  //     case p: Mirror.ProductOf[A] => derivePrettyStringCaseClass(using p)

  // inline def derivePrettyStringSealedTrait[A](using m: Mirror.SumOf[A]) =
  //   new PrettyString[A]:
  //     def prettyString(a: A): String =
  //       // label and elemLabels not needed
  //       // same as for the case class
  //       val elemInstances = getTypeclassInstances[m.MirroredElemTypes]
  //       // Checks the ordinal of tye type, e.g. 0 for User or 1 for AnonymousVisitor
  //       val elemOrdinal = m.ordinal(a)
  //       elemInstances(elemOrdinal).prettyString(a)

  // inline def derivePrettyStringCaseClass[A](using m: Mirror.ProductOf[A]) =
  //   new PrettyString[A]:
  //     def prettyString(a: A): String =
  //       val label = labelFromMirror[m.MirroredType]
  //       val elemLabels = getElemLabels[m.MirroredElemLabels]
  //       val elemInstances = getTypeclassInstances[m.MirroredElemTypes]
  //       val elems = a.asInstanceOf[Product].productIterator
  //       val elemStrings = elems.zip(elemLabels).zip(elemInstances).map {
  //         case ((elem, label), instance) => s"$label=${instance.prettyString(elem)}"
  //       }
  //       if elemLabels.isEmpty then label else
  //         s"$label(${elemStrings.mkString(", ")})"

  // inline def labelFromMirror[A](using m: Mirror.Of[A]): String = constValue[m.MirroredLabel]

  // inline def getElemLabels[A <: Tuple]: List[String] = inline erasedValue[A] match
  //   // stop condition - the tuple is empty
  //   case _: EmptyTuple => Nil
  //   // yes, in scala 3 we can match on tuples head and tail to deconstruct them step by step
  //   case _: (head *: tail) =>
  //     // bring the head label to value space
  //     val headElementLabel = constValue[head].toString
  //     // recursive call to get the labels from the tail
  //     val tailElementLabels = getElemLabels[tail]
  //     // concat head + tail
  //     headElementLabel :: tailElementLabels

  // inline def getTypeclassInstances[A <: Tuple]: List[PrettyString[Any]] = inline erasedValue[A] match
  //   case _: EmptyTuple => Nil
  //   case _: (head *: tail) =>
  //     // summon was known as implicitly in scala 2
  //     val headTypeClass = summonInline[PrettyString[head]]
  //     // recursive call to resolve also the tail
  //     val tailTypeClasses = getTypeclassInstances[tail]
  //     headTypeClass.asInstanceOf[PrettyString[Any]] :: tailTypeClasses

  // inline def getElemLabelsHelpler[A](using m: Mirror.Of[A]) =
  //   // and call getElemLabels with the elemlabels type
  //   getElemLabels[m.MirroredElemLabels]

  // inline def summonInstancesHelper[A](using m: Mirror.Of[A]) = getTypeclassInstances[m.MirroredElemTypes]
