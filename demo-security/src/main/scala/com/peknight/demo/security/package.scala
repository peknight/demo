package com.peknight.demo

import fs2.{Chunk, Pipe, Pull, Stream, text}

package object security:

  def scanChunkTimesNLast[F[_], I, O, S](n: Int)(init: => S)
                                    (f: (S, Chunk[I]) => (S, Chunk[O]))
                                    (lastF: (S, Chunk[I]) => Chunk[O]): Pipe[F, I, O] =
    assert(n > 0, s"n($n) must be positive!")
    def go(acc: Chunk[I], current: S, s: Stream[F, I]): Pull[F, O, Option[(S, Stream[F, I])]] =
      s.pull.uncons.flatMap {
        // 流结束，处理最后一组数据
        case None => if acc.isEmpty then Pull.pure(None) else Pull.output(lastF(current, acc)).as(None)
        case Some((hd, tl)) =>
          val headSize = hd.size
          // 判断此前剩余Chunk + 当前Chunk大小
          val size = acc.size + headSize
          // 小于等于n 打包留给流后续处理
          if size <= n then go(acc ++ hd, current, tl) else
            val mod = size % n
            val lastSize = if mod == 0 then n else mod
            if lastSize == headSize then
              val (next, os) = f(current, acc)
              Pull.output(os) >> go(hd, next, tl)
            else
              val (pfx, sfx) = hd.splitAt(headSize - lastSize)
              val (next, os) = f(current, acc ++ pfx)
              Pull.output(os) >> go(sfx, next, tl)
      }
    end go
    in => Pull.loop((tuple: (S, Stream[F, I])) => go(Chunk.empty, tuple._1, tuple._2))((init, in)).stream
  end scanChunkTimesNLast

  def scanChunkTimesN[F[_], I, O, S](n: Int)(init: => S)(f: (S, Chunk[I]) => (S, Chunk[O])): Pipe[F, I, O] =
    assert(n > 0, s"n($n) must be positive!")
    def go(acc: Chunk[I], current: S, s: Stream[F, I]): Pull[F, O, Option[(S, Stream[F, I])]] =
      s.pull.uncons.flatMap {
        // 流结束，处理最后一组数据
        case None => if acc.isEmpty then Pull.pure(None) else Pull.output(f(current, acc)._2).as(None)
        case Some((hd, tl)) =>
          val headSize = hd.size
          // 判断此前剩余Chunk + 当前Chunk大小
          val size = acc.size + headSize
          // 小于n 打包留给流后续处理
          if size < n then go(acc ++ hd, current, tl)
          else if size % n == 0 then
            val (next, os) = f(current, acc ++ hd)
            Pull.output(os).as(Some((next, tl)))
          else
            val (pfx, sfx) = hd.splitAt(headSize - size % n)
            val (next, os) = f(current, acc ++ pfx)
            Pull.output(os).as(Some((next, tl.cons(sfx))))
      }
    end go
    in => Pull.loop((tuple: (S, Stream[F, I])) => go(Chunk.empty, tuple._1, tuple._2))((init, in)).stream
  end scanChunkTimesN

  def mapChunkTimesNLast[F[_], I, O](n: Int)(f: Chunk[I] => Chunk[O])(lastF: Chunk[I] => Chunk[O]): Pipe[F, I, O] =
    assert(n > 0, s"n($n) must be positive!")
    def go(acc: Chunk[I], s: Stream[F, I]): Pull[F, O, Option[Stream[F, I]]] =
      s.pull.uncons.flatMap {
        // 流结束，处理最后一组数据
        case None => if acc.isEmpty then Pull.pure(None) else Pull.output(lastF(acc)).as(None)
        case Some((hd, tl)) =>
          val headSize = hd.size
          // 判断此前剩余Chunk + 当前Chunk大小
          val size = acc.size + headSize
          // 小于等于n 打包留给流后续处理
          if size <= n then go(acc ++ hd, tl) else
            val mod = size % n
            val lastSize = if mod == 0 then n else mod
            if lastSize == headSize then Pull.output(f(acc)) >> go(hd, tl) else
              val (pfx, sfx) = hd.splitAt(headSize - lastSize)
              Pull.output(f(acc ++ pfx)) >> go(sfx, tl)
      }
    end go
    in => Pull.loop((s: Stream[F, I]) => go(Chunk.empty, s))(in).stream
  end mapChunkTimesNLast

  def mapChunkTimesN[F[_], I, O](n: Int)(f: Chunk[I] => Chunk[O]): Pipe[F, I, O] =
    assert(n > 0, s"n($n) must be positive!")
    def go(acc: Chunk[I], s: Stream[F, I]): Pull[F, O, Option[Stream[F, I]]] =
      s.pull.uncons.flatMap {
        // 流结束，处理最后一组数据
        case None => if acc.isEmpty then Pull.pure(None) else Pull.output(f(acc)).as(None)
        case Some((hd, tl)) =>
          val headSize = hd.size
          // 判断此前剩余Chunk + 当前Chunk大小
          val size = acc.size + headSize
          // 小于n 打包留给流后续处理
          if size < n then go(acc ++ hd, tl)
          else if size % n == 0 then Pull.output(f(acc ++ hd)).as(Some(tl))
          else
            val (pfx, sfx) = hd.splitAt(headSize - size % n)
            Pull.output(f(acc ++ pfx)).as(Some(tl.cons(sfx)))
      }
    end go
    in => Pull.loop((s: Stream[F, I]) => go(Chunk.empty, s))(in).stream
  end mapChunkTimesN

  extension (bytes: Array[Byte])
    def hex: String = Stream.chunk(Chunk.array(bytes)).through(text.hex.encode).toList.mkString("")
    def utf8: String = Stream.chunk(Chunk.array(bytes)).through(text.utf8.decode).toList.mkString("")
