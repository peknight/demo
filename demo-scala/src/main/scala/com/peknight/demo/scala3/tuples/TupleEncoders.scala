package com.peknight.demo.scala3.tuples

object TupleEncoders:
  // Base case
  given RowEncoder[EmptyTuple] with
    def encodeRow(empty: EmptyTuple) =
      List.empty

  // Inductive case
  given [H: FieldEncoder, T <: Tuple: RowEncoder]: RowEncoder[H *: T] with
    def encodeRow(tuple: H *: T) =
      summon[FieldEncoder[H]].encodeField(tuple.head) :: summon[RowEncoder[T]].encodeRow(tuple.tail)
end TupleEncoders

