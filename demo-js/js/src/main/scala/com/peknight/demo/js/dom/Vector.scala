package com.peknight.demo.js.dom

trait Vector[U]:
  def x: U
  def y: U
  def +(v: Vector[U]): Vector[U]
  def -(v: Vector[U]): Vector[U]
  def *(u: U): Vector[U]
  def /(u: U): Vector[U]
  def length: U
