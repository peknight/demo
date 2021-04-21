package com.peknight.demo.fpinscala.state

case class State[S, +A](run: S => (A, S))
