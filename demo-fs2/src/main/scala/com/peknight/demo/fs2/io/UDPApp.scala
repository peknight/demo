package com.peknight.demo.fs2.io

import cats.effect.std.Console
import cats.effect.{Concurrent, IO, IOApp}
import com.comcast.ip4s.{SocketAddress, ip, port}
import fs2.io.net.{Datagram, DatagramSocket, Network}
import fs2.{Stream, text}

import scala.concurrent.duration.DurationInt

object UDPApp extends IOApp.Simple:

  val address = SocketAddress(ip"127.0.0.1", port"5555")

  def socketWrites[F[_]](socket: DatagramSocket[F]) =
    Stream("Hello, world!")
      .through(text.utf8.encode)
      .chunks
      .map(data => Datagram(address, data))
      .through(socket.writes)
      .drain

  def socketReads[F[_]: Console](socket: DatagramSocket[F]) =
    socket.reads
      .flatMap(datagram => Stream.chunk(datagram.bytes))
      .through(text.utf8.decode)
      .foreach { response =>
        Console[F].println(s"Response: $response")
      }

  def client[F[_]: Concurrent: Console: Network]: F[Unit] = {
    Stream.resource(Network[F].openDatagramSocket()).flatMap { socket =>
        socketWrites(socket) ++ socketReads(socket)
    }.compile.drain
  }

  def echoServer[F[_]: Concurrent: Network]: F[Unit] =
    Stream.resource(Network[F].openDatagramSocket(port = Some(port"5555"))).flatMap {
      socket => socket.reads.through(socket.writes)
    }.compile.drain


  val run = Stream(
    Stream.eval(echoServer[IO]).interruptAfter(5.seconds),
    Stream.eval(client[IO]).interruptAfter(5.seconds)
  ).parJoin(2).compile.drain
