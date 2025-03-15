package com.peknight.demo.fs2.io

import cats.effect.*
import cats.effect.std.Console
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.io.net.{ConnectException, Network, Socket}
import fs2.{Chunk, Stream, text}

import scala.concurrent.duration.DurationInt

/**
 * The <a href="https://github.com/typelevel/fs2-chat">fs2-chat</a> sample application
 * implements a multiuser chat server and a single user chat client
 * using the FS2 TCP support and <a href="https://scodec.org/">scodec</a> for binary processing.
 */
object TCPApp extends IOApp.Simple:

  val socketAddress = SocketAddress(host"localhost", port"5555")

  def socketResource[F[_]: Network](address: SocketAddress[Host]) = Network[F].client(address)

  def clientV1[F[_]: {MonadCancelThrow, Console, Network}]: F[Unit] =
    socketResource[F](socketAddress).use { socket =>
      socket.write(Chunk.array("Hello, world!".getBytes)) >>
        socket.read(8192).flatMap { response =>
          Console[F].println(s"Response: $response")
        }
    }

  def socketStream[F[_]: {MonadCancelThrow, Network}](address: SocketAddress[Host]) =
    Stream.resource(socketResource[F](address))

  def clientV2[F[_]: {MonadCancelThrow, Console, Network}]: Stream[F, Unit] =
    socketStream[F](socketAddress).flatMap { socket =>
      Stream("Hello, world!").through(text.utf8.encode).through(socket.writes) ++
        socket.reads.through(text.utf8.decode).foreach { response => Console[F].println(s"Response: $response") }
    }

  def socketWrites[F[_]](socket: Socket[F]) =
    Stream("Hello, world!")
      .interleave(Stream.constant("\n"))
      .through(text.utf8.encode)
      .through(socket.writes)

  def socketReads[F[_]: Console](socket: Socket[F]) =
    socket.reads
      .through(text.utf8.decode)
      .through(text.lines)
      .head
      .foreach { response => Console[F].println(s"Response: $response") }

  def clientV3[F[_]: {MonadCancelThrow, Console, Network}]: Stream[F, Unit] =
    socketStream[F](socketAddress).flatMap { socket => socketWrites(socket) ++ socketReads(socket) }

  def connect[F[_]: {Temporal, Network}](address: SocketAddress[Host]): Stream[F, Socket[F]] =
    socketStream(address).handleErrorWith {
      case _: ConnectException => connect(address).delayBy(5.seconds)
    }

  def client[F[_]: {Temporal, Console, Network}]: Stream[F, Unit] =
    connect(socketAddress).flatMap { socket => socketWrites(socket) ++ socketReads(socket) }

  def echoServer[F[_]: {Concurrent, Network}]: F[Unit] = Network[F].server(port = Some(port"5555")).map { client =>
    client.reads
      .through(text.utf8.decode)
      .through(text.lines)
      .interleave(Stream.constant("\n"))
      .through(text.utf8.encode)
      .through(client.writes)
      .handleErrorWith(_ => Stream.empty)
  }.parJoin(100).compile.drain

  val run = Stream(
    Stream.eval(echoServer[IO]).interruptAfter(5.seconds),
    client[IO]
  ).parJoin(2).compile.drain
