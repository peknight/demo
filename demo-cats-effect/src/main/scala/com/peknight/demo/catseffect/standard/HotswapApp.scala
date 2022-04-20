package com.peknight.demo.catseffect.standard

import cats.effect.std.Hotswap
import cats.effect.{IO, Ref, Resource}

import java.io.File

/**
 * 在`Resource#use`内构造新的`Resource`会造成外层资源在内层资源释放前无法释放导致内存泄露。
 * 比如日志每打印n字节就会滚动切换到新日志文件的场景
 */
object HotswapApp:

  trait Logger[F[_]]:
    def log(msg: String): F[Unit]

  def rotating(n: Int): Resource[IO, Logger[IO]] = Hotswap.create[IO, File].flatMap { hs =>
    def file(name: String): Resource[IO, File] = ???
    def write(file: File, msg: String): IO[Unit] = ???

    Resource.eval {
      for
        index <- Ref[IO].of(0)
        count <- Ref[IO].of(0)
        // Open the initial log file
        f <- hs.swap(file("0.log"))
        logFile <- Ref[IO].of(f)
      yield
        new Logger[IO]:
          def log(msg: String): IO[Unit] =
            count.get.flatMap { currentCount =>
              if msg.length() < n - currentCount then
                for
                  currentFile <- logFile.get
                  _ <- write(currentFile, msg)
                  _ <- count.update(_ + msg.length())
                yield ()
              else
                for
                  // Reset the log length counter
                  _ <- count.set(msg.length())
                  // Increment the counter for the log file name
                  idx <- index.updateAndGet(_ + 1)
                  // Close the old log file and open the new one
                  f <- hs.swap(file(s"$idx.log"))
                  _ <- logFile.set(f)
                  _ <- write(f, msg)
                yield ()
            }
    }
  }
