package com.peknight.demo.fs2.io

import cats.effect.std.Console
import cats.effect.{MonadCancelThrow, Resource}
import cats.implicits.{catsSyntaxFlatMapOps, toFunctorOps}
import com.comcast.ip4s.{Host, IpLiteralSyntax, SocketAddress}
import fs2.io.net.Network
import fs2.io.net.tls.{TLSContext, TLSParameters, TLSSocket}
import fs2.{Chunk, Stream, text}

import javax.net.ssl.SNIHostName

object TLSApp {

  def socketWrites[F[_]](socket: TLSSocket[F]) =
    Stream("Hello, world!")
      .interleave(Stream.constant("\n"))
      .through(text.utf8.encode)
      .through(socket.writes)

  def socketReads[F[_]: Console](socket: TLSSocket[F]) =
    socket.reads
      .through(text.utf8.decode)
      .through(text.lines)
      .head
      .foreach { response =>
        Console[F].println(s"Response: $response")
      }

  def client[F[_]: MonadCancelThrow: Console: Network](tlsContext: TLSContext[F]): Stream[F, Unit] = {
    Stream.resource(Network[F].client(SocketAddress(host"localhost", port"5555"))).flatMap { underlyingSocket =>
      Stream.resource(tlsContext.client(underlyingSocket)).flatMap { socket =>
          socketWrites(socket) ++ socketReads(socket)
      }
    }
  }

  def tlsClientWithSni[F[_]: MonadCancelThrow: Network](tlsContext: TLSContext[F],
                                                        address: SocketAddress[Host]): Resource[F, TLSSocket[F]] =
    Network[F].client(address).flatMap { underlyingSocket =>
      tlsContext.clientBuilder(underlyingSocket)
        .withParameters(TLSParameters(
          protocols = Some(List("TLSv1.3")),
          serverNames = Some(List(new SNIHostName(address.host.toString)))
        ))
        .build
    }

  def debug[F[_]: MonadCancelThrow: Network](tlsContext: TLSContext[F], address: SocketAddress[Host]): F[String] =
    Network[F].client(address).use { underlyingSocket =>
      tlsContext.clientBuilder(underlyingSocket)
        .withParameters(TLSParameters(serverNames = Some(List(new SNIHostName(address.host.toString)))))
        .build
        .use { tlsSocket =>
          tlsSocket.write(Chunk.empty) >> tlsSocket.session.map { session =>
            s"Cipher suite: ${session.getCipherSuite}\r\n" +
              "Peer certificate chain:\r\n" +
              session.getPeerCertificates
                .zipWithIndex
                .map { case (cert, idx) => s"Certificate $idx: $cert" }
                .mkString("\r\n")
          }
        }
    }


}
