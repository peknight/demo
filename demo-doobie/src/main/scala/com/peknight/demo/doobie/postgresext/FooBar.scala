package com.peknight.demo.doobie.postgresext

enum FooBar derives CanEqual:
  case Foo
  case Bar

object FooBar:
  def toEnum(e: FooBar): String = e match
    case Foo => "foo"
    case Bar => "bar"

  def fromEnum(s: String): Option[FooBar] =
    Option(s) collect {
      case "foo" => Foo
      case "bar" => Bar
    }


