package com.peknight.demo.fpinscala.iomonad

import com.peknight.demo.fpinscala.parallelism.Nonblocking.Par

import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, CompletionHandler}

trait Source {
  def readBytes(numBytes: Int, callback: Either[Throwable, Array[Byte]] => Unit): Unit
}
object Source {

  def nonblockingRead(source: Source, numBytes: Int): Par[Either[Throwable, Array[Byte]]] = Par.async {
    (cb: Either[Throwable, Array[Byte]] => Unit) => source.readBytes(numBytes, cb)
  }

  def readPar(source: Source, numBytes: Int): Free[Par, Either[Throwable, Array[Byte]]] =
    Free.Suspend(nonblockingRead(source, numBytes))

  // Exercise 13.5

  def read(file: AsynchronousFileChannel, fromPosition: Long, numBytes: Int): Par[Either[Throwable, Array[Byte]]] =
    Par.async { (cb: Either[Throwable, Array[Byte]] => Unit) =>
      val buf = ByteBuffer.allocate(numBytes)
      file.read(buf, fromPosition, (), new CompletionHandler[Integer, Unit] {
        def completed(bytesRead: Integer, ignore: Unit) = {
          val arr = new Array[Byte](bytesRead)
          buf.slice.get(arr, 0, bytesRead)
          cb(Right(arr))
        }
        def failed(err: Throwable, ignore: Unit) = cb(Left(err))
      })
    }
}
