package com.peknight.demo.fpinscala.errorhandling

sealed trait Option[+A] {

  import Option._

  def map[B](f: A => B): Option[B] = this match {
    case Some(a) => Some(f(a))
    case None => None
  }

  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case Some(a) => f(a)
    case None => None
  }

  def flatMapViaGetOrElse[B](f: A => Option[B]): Option[B] =
    map(f) getOrElse None

  def getOrElse[B >: A](default: => B): B = this match {
    case Some(a) => a
    case None => default
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
    case oa @ Some(_) => oa
    case None => ob
  }

  def orElseViaGetOrElse[B >: A](ob: => Option[B]): Option[B] =
    this map (Some(_)) getOrElse ob

  def filter(f: A => Boolean): Option[A] = this match {
    case Some(a) if f(a)=> this
    case _ => None
  }

  def filterViaFlatMap(f: A => Boolean): Option[A] =
    flatMap(a => if (f(a)) Some(a) else None)
}

object Option {
  case class Some[+A](get: A) extends Option[A]
  case object None extends Option[Nothing]
}