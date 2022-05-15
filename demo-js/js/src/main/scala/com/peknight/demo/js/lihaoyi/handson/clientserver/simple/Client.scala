package com.peknight.demo.js.lihaoyi.handson.clientserver.simple

import cats.effect.*
import cats.effect.unsafe.implicits.*
import cats.syntax.traverse.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.client.dsl.io.*
import org.http4s.dom.*
import org.http4s.dsl.io.*
import org.http4s.syntax.all.*
import org.scalajs.dom
import org.scalajs.dom.*
import scalatags.JsDom.all.{html as _, *}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("ClientServer")
object Client:

  val client = FetchClientBuilder[IO].create

  @JSExport
  def main(container: html.Div): Unit =
    val inputBox = input.render
    val outputBox = ul.render
    inputBox.onkeyup = (_: dom.Event) => update.unsafeRunAndForget()
    program.unsafeRunAndForget()

    def update: IO[Unit] =
      for
        fileDataSeq <- client.expect[Seq[FileData]](POST(FilePath(inputBox.value).asJson, uri"/ajax/list"))
        _ <- IO(outputBox.innerHTML = "")
        _ <- renderFileDataSeq(fileDataSeq)
      yield ()
    end update

    def renderFileDataSeq(fileDataSeq: Seq[FileData]): IO[Unit] =
      val lis =
        for
          FileData(name, size) <- fileDataSeq
        yield
          li(b(name), "-", size, "bytes")
      lis.map(l => IO(outputBox.appendChild(l.render))).sequence.as(())
    end renderFileDataSeq

    def program: IO[Unit] =
      for
        _ <- update
        _ <- IO(container.appendChild(div(h1("File Search"), inputBox, outputBox).render))
      yield ()

  end main






