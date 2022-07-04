package com.peknight.demo

import fs2.{Chunk, Pipe, Pull, Stream, text}

package object security:

  def block[F[_], I, O](n: Int, noPad: Chunk[I] => Chunk[O], pad: Chunk[I] => Chunk[O]): Pipe[F, I, O] =
    def go(acc: Chunk[I], s: Stream[F, I]): Pull[F, O, Option[Stream[F, I]]] =
      s.pull.uncons.flatMap {
        // 流结束，使用填充方法
        case None => Pull.output(pad(acc)).as(None)
        case Some((hd, tl)) =>
          // 判断此前剩余Chunk + 当前Chunk大小
          val size = acc.size + hd.size
          // 小于n 打包留给流后续处理
          if size < n then go(acc ++ hd, tl)
          // 正好是整数倍，直接输出
          else if size % n == 0 then Pull.output(noPad(acc ++ hd)).as(Some(tl))
          else
            // 多余整数倍，把零头(sfx)提出来
            val (pfx, sfx) = hd.splitAt(hd.size - size % n)
            // 将整数倍的部分输出，零头拼到流的后续
            Pull.output(noPad(acc ++ pfx)).as(Some(tl.cons(sfx)))
      }
    in => in.repeatPull(pull => go(Chunk.empty, pull.echo.stream))

  extension (bytes: Array[Byte])
    def hex: String = Stream.chunk(Chunk.array(bytes)).through(text.hex.encode).toList.mkString("")
    def utf8: String = Stream.chunk(Chunk.array(bytes)).through(text.utf8.decode).toList.mkString("")
