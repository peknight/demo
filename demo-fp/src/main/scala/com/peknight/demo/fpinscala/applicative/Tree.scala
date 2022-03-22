package com.peknight.demo.fpinscala.applicative

case class Tree[+A](head: A, tail: List[Tree[A]])
