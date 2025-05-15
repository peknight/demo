package com.peknight.demo.tyrian.guide

import cats.effect.IO
import org.scalajs.dom.html
import tyrian.*
import tyrian.cmds.*


object BuiltInGoodiesApp:
  enum Msg:
    case Read(contents: String)
    case UseImage(img: html.Image)
    case RandomValue(value: String)
    case Error(message: String)
    case Empty
    case NoOp
  end Msg

  // Dom
  // Assuming two messages Error and Empty, we can attempt to focus a given ID.
  val domCmd: Cmd[IO, Msg] =
    Dom.focus("my-id") {
      case Left(Dom.NotFound(id)) => Msg.Error(s"ID $id not found")
      case Right(_) => Msg.Empty
    }
  // Dom.blur works in the same way, though, performing the opposite effect.

  import tyrian.cmds.*

  // FileReader
  // Will read any file data, with build in support for text and images.
  // Assuming two messages Error and Read, we can attempt to read the contents of a text file.
  val fileReaderCmd: Cmd[IO, Msg] =
    FileReader.readText("my-file-input-field-id") {
      case FileReader.Result.Error(msg) => Msg.Error(msg)
      case FileReader.Result.File(name, path, contents) => Msg.Read(contents)
      case FileReader.Result.NoFile(_) => Msg.Empty
    }

  // ImageLoader
  // Given a path, this cmd will load an image and create and return an HTMLImageElement for you to make use of.

  val imageLoaderCmd: Cmd[IO, Msg] =
    ImageLoader.load("path/to/img.png") {
      case ImageLoader.Result.ImageLoadError(msg, path) => Msg.Error(msg)
      case ImageLoader.Result.Image(imageElement) => Msg.UseImage(imageElement)
    }

  // LocalStorage
  // A series of commands that mirror the localstorage interface.
  val localStorageCmd: Cmd[IO, Msg] = {
    given CanEqual[LocalStorage.Result, LocalStorage.Result] = CanEqual.derived
    Cmd.Batch[IO, Msg](
      LocalStorage.setItem("key", "value") {
        case LocalStorage.Result.Success => Msg.NoOp
        case e => Msg.Error(e.toString)
      },
      LocalStorage.getItem("key") {
        case Right(LocalStorage.Result.Found(value)) => Msg.Read(value)
        case Left(LocalStorage.Result.NotFound(e)) => Msg.Error(e)
      },
      LocalStorage.removeItem("key") {
        case LocalStorage.Result.Success => Msg.NoOp
        case e => Msg.Error(e.toString)
      },
      LocalStorage.clear {
        case LocalStorage.Result.Success => Msg.NoOp
        case e => Msg.Error(e.toString)
      },
      LocalStorage.key(0) {
        case LocalStorage.Result.Key(keyAtIndex0) => Msg.Read(keyAtIndex0)
        case LocalStorage.Result.NotFound(e) => Msg.Error(e)
        case e => Msg.Error(e.toString)
      },
      LocalStorage.length {
        case LocalStorage.Result.Length(value) => Msg.Read(value.toString)
      }
    )
  }
end BuiltInGoodiesApp
