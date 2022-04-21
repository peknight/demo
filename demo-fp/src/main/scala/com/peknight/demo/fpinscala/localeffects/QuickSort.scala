package com.peknight.demo.fpinscala.localeffects

import com.peknight.demo.fpinscala.localeffects.ST.{STArray, STRef}

object QuickSort:

  def quickSortOrigin(xs: List[Int]): List[Int] =
    if xs.isEmpty then xs
    else
      val arr = xs.toArray

      def swap(x: Int, y: Int) =
        val tmp = arr(x)
        arr(x) = arr(y)
        arr(y) = tmp

      // Partitions a portion of the array into elements less than and greater than pivot, respectively
      def partition(n: Int, r: Int, pivot: Int) =
        val pivotVal = arr(pivot)
        swap(pivot, r)
        var j = n
        for (i <- n until r) do
          if arr(i) < pivotVal then
            swap(i, j)
            j += 1
        end for
        swap(j, r)
        j
      end partition


      // Sorts a portion of the array in place
      def qs(n: Int, r: Int): Unit =
        if n < r then
          // 英文版这里pivot传参错传为n + (n - r) / 2，中文译版是正确的（可能我看的英文版本太旧了）
          val pi = partition(n, r, n + (r - n) / 2)
          qs(n, pi - 1)
          qs(pi + 1, r)
      end qs

      qs(0, arr.length - 1)
      arr.toList

    end if
  end quickSortOrigin

  def swap[S](arr: STArray[S, Int], i: Int, j: Int): ST[S, Unit] =
    for
      x <- arr.read(i)
      y <- arr.read(j)
      _ <- arr.write(i, y)
      _ <- arr.write(j, x)
    yield ()

  // Exercise 14.2

  def partition[S](arr: STArray[S, Int], n: Int, r: Int, pivot: Int): ST[S, Int] =
    for
      pivotVal <- arr.read(pivot)
      _ <- swap(arr, pivot, r)
      j <- STRef(n)
      _ <- (n until r).foldLeft(ST.noop[S])((s, i) =>
        for
          _ <- s
          vi <- arr.read(i)
          _ <-
            if vi < pivotVal then
              for
                vj <- j.read
                _ <- swap(arr, i, vj)
                _ <- j.write(vj + 1)
              yield ()
            else ST.noop[S]
        yield ())
      x <- j.read
      _ <- swap(arr, x, r)
    yield x

  def qs[S](a: STArray[S, Int], n: Int, r: Int): ST[S, Unit] =
    if n < r then
      for
        pi <- partition(a, n, r, n + (r - n) / 2)
        _ <- qs(a, n, pi - 1)
        _ <- qs(a, pi + 1, r)
      yield ()
    else ST.noop[S]

  def quickSort(xs: List[Int]): List[Int] = if xs.isEmpty then xs else
    ST.runST(new RunnableST[List[Int]]:
      def apply[S] =
        for
          arr <- STArray.fromList(xs)
          size <- arr.size
          _ <- qs(arr, 0, size - 1)
          sorted <- arr.freeze
        yield sorted
    )

end QuickSort
