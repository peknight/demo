package com.peknight.demo.shapeless

import scala.annotation.targetName
import scala.compiletime.{constValue, erasedValue, summonInline}
import scala.deriving.Mirror

package object generic:

  //noinspection DuplicatedCode
  inline def summonAsTuple[A <: Tuple]: A = inline erasedValue[A] match
    case _: EmptyTuple => EmptyTuple.asInstanceOf[A]
    case _: (h *: t) => (summonInline[h] *: summonAsTuple[t]).asInstanceOf[A]

  //noinspection DuplicatedCode
  inline def summonValuesAsTuple[A <: Tuple]: A = inline erasedValue[A] match
    case _: EmptyTuple => EmptyTuple.asInstanceOf[A]
    case _: (h *: t) => (constValue[h] *: summonValuesAsTuple[t]).asInstanceOf[A]

  def unexpected: Nothing = sys.error("Unexpected invocation")

  @targetName("TypeInequalities")
  trait =:!=[A, B] extends Serializable
  @targetName("TypeInequalitiesObject")
  object =:!= {
    given[A, B]: =:!=[A, B] = new =:!=[A, B] {}

    // 妙啊
    given neqAmbiguous1[A]: =:!=[A, A] = unexpected

    given neqAmbiguous2[A]: =:!=[A, A] = unexpected
  }

  /**
   * Type class witnessing that L doesn't contain elements of type U
   */
  trait NotContainsConstraint[L <: Tuple, U] extends Serializable
  object NotContainsConstraint:
    def apply[L <: Tuple, U](using ncc: NotContainsConstraint[L, U]): NotContainsConstraint[L, U] = ncc
    type NotContains[U] = {
      type λ[L <: Tuple] = NotContainsConstraint[L, U]
    }
    given [U]: NotContainsConstraint[EmptyTuple, U] = new NotContainsConstraint[EmptyTuple, U] {}
    given [H, T <: Tuple, U](using nc: T NotContainsConstraint U, neq: U =:!= H): NotContainsConstraint[H *: T, U] =
      new NotContainsConstraint[H *: T, U] {}
  end NotContainsConstraint

  /**
   * Type class witnessing that all elements of L have distinct types
   */
  trait IsDistinctConstraint[L <: Tuple] extends Serializable
  object IsDistinctConstraint:
    def apply[L <: Tuple](using idc: IsDistinctConstraint[L]): IsDistinctConstraint[L] = idc
    given IsDistinctConstraint[EmptyTuple] = new IsDistinctConstraint[EmptyTuple] {}
    given [H, T <: Tuple](using d: IsDistinctConstraint[T], nc: NotContainsConstraint[T, H]): IsDistinctConstraint[H *: T] =
      new IsDistinctConstraint[H *: T] {}
  end IsDistinctConstraint

  /** Dependent unary function type. */
  trait DepFn1[T]:
    type Out
    def apply(t: T): Out
  end DepFn1

  /** Dependent binary function type. */
  trait DepFn2[T, U]:
    type Out
    def apply(t: T, u: U): Out
  end DepFn2

  /**
   * Type class supporting removal of an element from this `Tuple`. Available only if this `Tuple` contains an
   * element of type `E`.
   *
   * @author Stacy Curl
   */
  trait Remove[L <: Tuple, E] extends DepFn1[L] with Serializable:
    def reinsert(out: Out): L
  end Remove
  trait LowPriorityRemove:
    type Aux[L <: Tuple, E, Out0] = Remove[L, E] { type Out = Out0 }
    given [H, T <: Tuple, E, OutT <: Tuple](using r: Aux[T, E, (E, OutT)]): Aux[H *: T, E, (E, H *: OutT)] =
      new Remove[H *: T, E]:
        type Out = (E, H *: OutT)
        def apply(l: H *: T): Out =
          val (e, tail) = r(l.tail)
          (e, l.head *: tail)
        def reinsert(out: Out): H *: T = out._2.head *: r.reinsert((out._1, out._2.tail))
  end LowPriorityRemove
  object Remove extends LowPriorityRemove:
    def apply[L <: Tuple, E](using remove: Remove[L, E]): Aux[L, E, remove.Out] = remove
    given [H, T <: Tuple]: Aux[H *: T, H, (H, T)] =
      new Remove[H *: T, H]:
        type Out = (H, T)
        def apply(l: H *: T): Out = (l.head, l.tail)
        def reinsert(out: Out): H *: T = out._1 *: out._2
  end Remove

  trait RemoveAll[L <: Tuple, SL <: Tuple] extends DepFn1[L] with Serializable:
    def reinsert(out: Out): L
  end RemoveAll
  object RemoveAll:
    type Aux[L <: Tuple, SL <: Tuple, Out0] = RemoveAll[L, SL] { type Out = Out0 }
    def apply[L <: Tuple, SL <: Tuple](using remove: RemoveAll[L, SL]): Aux[L, SL, remove.Out] = remove
    given [L <: Tuple]: Aux[L, EmptyTuple, (EmptyTuple, L)] =
      new RemoveAll[L, EmptyTuple]:
        type Out = (EmptyTuple, L)
        def apply(l: L): Out = (EmptyTuple, l)
        def reinsert(out: (EmptyTuple, L)): L = out._2
    given [L <: Tuple, E, RemE <: Tuple, Rem <: Tuple, SLT <: Tuple]
    (using rt: Remove.Aux[L, E, (E, RemE)], st: Aux[RemE, SLT, (SLT, Rem)]): Aux[L, E *: SLT, (E *: SLT, Rem)] =
      new RemoveAll[L, E *: SLT]:
        type Out = (E *: SLT, Rem)
        def apply(l: L): Out =
          val (e, rem) = rt(l)
          val (sl, left) = st(rem)
          (e *: sl, left)
        def reinsert(out: (E *: SLT, Rem)): L =
          rt.reinsert((out._1.head, st.reinsert((out._1.tail, out._2))))
  end RemoveAll

  /**
   * Type class supporting `Tuple` union. In case of duplicate types, this operation is a order-preserving multi-set union.
   * If type `T` appears n times in this `Tuple` and m > n times in `M`, the resulting `Tuple` contains the first n elements
   * of type `T` in this `Tuple`, followed by the last m - n element of type `T` in `M`.
   *
   * @author Olivier Blanvillain
   * @author Arya Irani
   */
  trait Union[L <: Tuple, M <: Tuple] extends DepFn2[L, M] with Serializable { type Out <: Tuple }
  trait LowPriorityUnion:
    type Aux[L <: Tuple, M <: Tuple, Out0 <: Tuple] = Union[L, M] { type Out = Out0 }
  end LowPriorityUnion
  object Union extends LowPriorityUnion:
    def apply[L <: Tuple, M <: Tuple](using union: Union[L, M]): Aux[L, M, union.Out] = union
    // let ∅ ∪ M = M
    given [M <: Tuple]: Aux[EmptyTuple, M, M] = new Union[EmptyTuple, M]:
      type Out = M
      def apply(l: EmptyTuple, m: M): Out = m
    // let (H *: T) ∪ M = H *: (T ∪ M) when H ∉ M
    given [H, T <: Tuple, M <: Tuple, U <: Tuple](using f: NotContainsConstraint[M, H], u: Aux[T, M, U])
    : Aux[H *: T, M, H *: U] = new Union[H *: T, M]:
      type Out = H *: U
      def apply(l: H *: T, m: M): Out = l.head *: u(l.tail, m)
    // let (H *: T) ∪ M  =  H *: (T ∪ (M - H)) when H ∈ M
    given [H, T <: Tuple, M <: Tuple, MR <: Tuple, U <: Tuple](using r: Remove.Aux[M, H, (H, MR)], u: Aux[T, MR, U])
    : Aux[H *: T, M, H *: U] = new Union[H *: T, M]:
      type Out = H *: U
      def apply(l: H *: T, m: M): Out = l.head *: u(l.tail, r(m)._2)
  end Union

  /**
   * Type class supporting `Tuple` intersection. In case of duplicate types, this operation is a multiset intersection.
   * If type `T` appears n times in this `Tuple` and m < n times in `M`, the resulting `Tuple` contains the first m
   * elements of type `T` in this `Tuple`.
   *
   * Also available if `M` contains types absent in this `Tuple`
   *
   * @author Olivier Blanvillain
   * @author Arya Irani
   */
  trait Intersection[L <: Tuple, M <: Tuple] extends DepFn1[L] with Serializable:
    type Out <: Tuple
  end Intersection
  trait LowPriorityIntersection:
    type Aux[L <: Tuple, M <: Tuple, Out0 <: Tuple] = Intersection[L, M] { type Out = Out0 }
  end LowPriorityIntersection
  object Intersection extends LowPriorityIntersection:
    def apply[L <: Tuple, M <: Tuple](using intersection: Intersection[L, M]): Aux[L, M, intersection.Out] =
      intersection
    // let ∅ ∩ M = ∅
    given [M <: Tuple]: Aux[EmptyTuple, M, EmptyTuple] = new Intersection[EmptyTuple, M]:
      type Out = EmptyTuple
      def apply(l: EmptyTuple): Out = EmptyTuple
    // let (H *: T) ∩ M = T ∩ M when H ∉ M
    given [H, T <: Tuple, M <: Tuple, I <: Tuple](using f: NotContainsConstraint[M, H], i: Aux[T, M, I])
    : Aux[H *: T, M, I] = new Intersection[H *: T, M]:
      type Out = I
      def apply(l: H *: T): Out = i(l.tail)
    // let (H *: T) ∩ M  =  H *: (T ∩ (M - H)) when H ∈ M
    given [H, T <: Tuple, M <: Tuple, MR <: Tuple, I <: Tuple](using r: Remove.Aux[M, H, (H, MR)], i: Aux[T, MR, I])
    : Aux[H *: T, M, H *: I] = new Intersection[H *: T, M]:
      type Out = H *: I
      def apply(l: H *: T): Out = l.head *: i(l.tail)
  end Intersection

  /**
   * Type class supporting `Tuple` subtraction. In case of duplicate types, this operation is a multiset difference.
   * If type `T` appears n times in this `Tuple` and m < n times in `M`, the resulting `Tuple` contains the last n - m
   * elements of type `T` in this `Tuple`.
   *
   * Also available if `M` contains types absent in this `Tuple`.
   *
   * @author Olivier Blanvillain
   */
  trait Diff[L <: Tuple, M <: Tuple] extends DepFn1[L] with Serializable { type Out <: Tuple }
  trait LowPriorityDiff:
    type Aux[L <: Tuple, M <: Tuple, Out0] = Diff[L, M] { type Out = Out0 }
    given [L <: Tuple, H, T <: Tuple, D <: Tuple](using d: Aux[L, T, D]): Aux[L, H *: T, D] = new Diff[L, H *: T]:
      type Out = D
      def apply(l: L): Out = d(l)
  end LowPriorityDiff
  object Diff extends LowPriorityDiff:
    def apply[L <: Tuple, M <: Tuple](using diff: Diff[L, M]): Aux[L, M, diff.Out] = diff
    given [L <: Tuple]: Aux[L, EmptyTuple, L] = new Diff[L, EmptyTuple]:
      type Out = L
      def apply(l: L): Out = l
    given [L <: Tuple, LT <: Tuple, H, T <: Tuple, D <: Tuple](using r: Remove.Aux[L, H, (H, LT)], d: Aux[LT, T, D])
    : Aux[L, H *: T, D] = new Diff[L, H *: T]:
      type Out = D
      def apply(l: L): Out = d(r(l)._2)
  end Diff

  /**
   * Type class supporting permuting this `HList` into the same order as another `HList` with
   * the same element types.
   *
   * @author Miles Sabin
   */
  trait Align[L <: Tuple, M <: Tuple] extends (L => M) with Serializable:
    def apply(l: L): M
  end Align
  object Align:
    def apply[L <: Tuple, M <: Tuple](using alm: Align[L, M]): Align[L, M] = alm
    given Align[EmptyTuple, EmptyTuple] with
      def apply(l: EmptyTuple): EmptyTuple = l
    given [L <: Tuple, MH, MT <: Tuple, R <: Tuple](using select: Remove.Aux[L, MH, (MH, R)], alignTail: Align[R, MT])
    : Align[L, MH *: MT] with
      def apply(l: L): MH *: MT =
        val (h, t) = select(l)
        h *: alignTail(t)
  end Align

  /**
   * Type class supporting prepending to this `HList`.
   *
   * @author Miles Sabin
   */
  trait Prepend[P <: Tuple, S <: Tuple] extends DepFn2[P, S] with Serializable { type Out <: Tuple }
  trait LowestPriorityPrepend:
    type Aux[P <: Tuple, S <: Tuple, Out0 <: Tuple] = Prepend[P, S] { type Out = Out0 }
    given [PH, PT <: Tuple, S <: Tuple, PtOut <: Tuple](using pt: Aux[PT, S, PtOut]): Aux[PH *: PT, S, PH *: PtOut] =
      new Prepend[PH *: PT, S]:
        type Out = PH *: PtOut
        def apply(prefix: PH *: PT, suffix: S): Out = prefix.head *: pt(prefix.tail, suffix)
  end LowestPriorityPrepend
  trait LowPriorityPrepend extends LowestPriorityPrepend:
    given [P <: Tuple, S <: EmptyTuple]: Aux[P, S, P] = new Prepend[P, S]:
      type Out = P
      def apply(prefix: P, suffix: S): P = prefix
  end LowPriorityPrepend
  object Prepend extends LowPriorityPrepend:
    def apply[P <: Tuple, S <: Tuple](using prepend: Prepend[P, S]): Aux[P, S, prepend.Out] = prepend
    given [P <: EmptyTuple, S <: Tuple]: Aux[P, S, S] = new Prepend[P, S]:
      type Out = S
      def apply(prefix: P, suffix: S): S = suffix
  end Prepend

  type Head[T] = T match { case h *: t => h }
  type Tail[T] = T match { case h *: t => t }

  type LiftP[F[_], T] <: Tuple =
    T match {
      case _ *: _ => F[Head[T]] *: LiftP[F, Tail[T]]
      case _ => EmptyTuple
    }

  type MirrorAux[A, Repr <: Tuple] = Mirror.Of[A] { type MirroredElemTypes = Repr }
  type MirrorLabelledAux[A, Labels <: Tuple, Repr <: Tuple] = Mirror.Of[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }

  type MirrorProductAux[A, Repr <: Tuple] = Mirror.ProductOf[A] { type MirroredElemTypes = Repr }
  type MirrorProductLabelledAux[A, Labels <: Tuple, Repr <: Tuple] = Mirror.ProductOf[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }

  type MirrorSumAux[A, Repr <: Tuple] = Mirror.SumOf[A] { type MirroredElemTypes = Repr }
  type MirrorSumLabelledAux[A, Labels <: Tuple, Repr <: Tuple] = Mirror.SumOf[A] {
    type MirroredElemLabels = Labels
    type MirroredElemTypes = Repr
  }
