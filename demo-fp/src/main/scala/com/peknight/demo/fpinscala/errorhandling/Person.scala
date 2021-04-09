package com.peknight.demo.fpinscala.errorhandling

import com.peknight.demo.fpinscala.errorhandling.Either._
import com.peknight.demo.fpinscala.errorhandling.Person._

case class Person(name: Name, age: Age)
object Person {
  sealed class Name(val value: String)
  sealed class Age(val value: Int)

  def mkName(name: String): Either[String, Name] =
    if (name == "" || name == null) Left("Name is empty.")
    else Right(new Name(name))

  def mkAge(age: Int): Either[String, Age] =
    if (age < 0) Left("Age is out of range.")
    else Right(new Age(age))

  def mkPerson(name: String, age: Int): Either[String, Person] = mkName(name).map2(mkAge(age))(Person(_, _))
}
