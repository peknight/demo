package com.peknight.demo.catseffect.tutorial.copyfile

import cats.effect._
import cats.syntax.all._

import java.io._

object CopyFile extends IOApp {

  def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] = Resource.make {
    Sync[F].blocking(new FileInputStream(f))
  } { inStream =>
    Sync[F].blocking(inStream.close()).handleErrorWith(_ => Sync[F].unit)
  }

  def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] = Resource.make {
    Sync[F].blocking(new FileOutputStream(f))
  } { outStream =>
    Sync[F].blocking(outStream.close()).handleError(_ => Sync[F].unit)
  }

  def inoutOutputStream[F[_]: Sync](in: File, out: File): Resource[F, (InputStream, OutputStream)] = for {
    inStream <- inputStream(in)
    outStream <- outputStream(out)
  } yield (inStream, outStream)

  // `fromAutoCloseable`这种方式虽然简洁一些，但不像`make`方式那样可以使用handleErrorWith处理关闭时的异常
  def inputStreamViaFromAutoCloseable(f: File): Resource[IO, FileInputStream] =
    Resource.fromAutoCloseable(IO(new FileInputStream(f)))

  def copy[F[_]: Sync](origin: File, destination: File): F[Long] = inoutOutputStream(origin, destination).use {
    case (in, out) => transfer[F](in, out)
  }

  /**
   * 这种方式虽然整体代码量比上面的少，但是看起来更为复杂一些。
   * 且存在弊端：如果输入流打开成功但输出流打开失败，那么打开的输入流将不会被关闭
   * 而上面使用Resource的方式通过flatMap决定输出流打开失败也会自动关闭输入流
   * 所以用到bracket方式的地方都要考虑是否可以用上面Resource的方式替换
   */
  def copyWithBracket(origin: File, destination: File): IO[Long] = {
    val inIO: IO[InputStream] = IO(new FileInputStream(origin))
    val outIO: IO[OutputStream] = IO(new FileOutputStream(destination))
    (inIO, outIO).tupled.bracket {
      case (in, out) => transfer[IO](in, out)
    } {
      case (in, out) =>
        (IO(in.close()), IO(out.close())).tupled.handleErrorWith(_ => IO.unit).void
    }
  }

  def transfer[F[_]: Sync](origin: InputStream, destination: OutputStream): F[Long] =
    transmit[F](origin, destination, new Array[Byte](1024 * 10), 0L)

  // 隐式转换上下文中存在Sync[IO]实例，这里可以将IO泛化为Sync
  def transmit[F[_]: Sync](origin: InputStream, destination: OutputStream, buffer: Array[Byte], acc: Long): F[Long] = for {
    // 阻塞的方法使用blocking而不是apply有助于cats-effect更好的规划调试线程
    amount <- Sync[F].blocking(origin.read(buffer, 0, buffer.size))
    count <- if (amount > -1) {
      Sync[F].blocking(destination.write(buffer, 0, amount)) >>
        transmit(origin, destination, buffer, acc + amount)
    } else Sync[F].pure(acc)
  } yield count

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- if (args.length < 2) IO.raiseError(new IllegalArgumentException("Need origin and destination files")) else IO.unit
    orig = new File(args(0))
    dest = new File(args(1))
    count <- copy[IO](orig, dest)
    _ <- IO.println(s"$count bytes copied from ${orig.getPath} to ${dest.getPath}")
  } yield ExitCode.Success

}
