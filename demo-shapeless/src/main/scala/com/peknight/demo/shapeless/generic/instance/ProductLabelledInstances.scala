package com.peknight.demo.shapeless.generic.instance

import com.peknight.demo.shapeless.generic.*
import scala.deriving.Mirror

class ProductLabelledInstances[F[_], A <: Product, Labels <: Tuple, Repr <: Tuple](
  mirror: MirrorProductLabelledAux[A, Labels, Repr], inst: => LiftP[F, Repr]):

  lazy val instances: LiftP[F, Repr] = inst

  inline def labelledInstances: Tuple.Zip[Labels, LiftP[F, Repr]] = summonValuesAsTuple[Labels].zip(instances)

  inline def zipWithLabelledInstances[T <: Tuple](t: T): Tuple.Zip[Tuple.Zip[Labels, LiftP[F, Repr]], T] =
    labelledInstances.zip(t)

  inline def zipWithLabelledInstances(a: A): Tuple.Zip[Tuple.Zip[Labels, LiftP[F, Repr]], Repr] =
    zipWithLabelledInstances(Tuple.fromProductTyped(a)(using mirror))
