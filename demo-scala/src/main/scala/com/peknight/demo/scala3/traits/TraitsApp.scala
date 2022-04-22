package com.peknight.demo.scala3.traits

object TraitsApp extends App:
  val basicIntQueue = new BasicIntQueue
  basicIntQueue.put(10)
  basicIntQueue.put(20)
  println(basicIntQueue.get())
  println(basicIntQueue.get())

  val myQueue = new MyQueue
  myQueue.put(10)
  println(myQueue.get())

  val basicIntQueueWithDoubling = new BasicIntQueue with Doubling
  basicIntQueueWithDoubling.put(10)
  println(basicIntQueueWithDoubling.get())

  // 最右特质最先被调用
  val queue1 = new BasicIntQueue with Incrementing with Filtering(_ >= 0)
  queue1.put(-1)
  queue1.put(0)
  queue1.put(1)
  println(queue1.get())
  println(queue1.get())

  val queue2 = new BasicIntQueue with Filtering(_ >= 0) with Incrementing
  queue2.put(-1)
  queue2.put(0)
  queue2.put(1)
  println(queue2.get())
  println(queue2.get())
  println(queue2.get())

  val frog = new Frog
  println(frog.philosophize)
  val duck = new Duck
  println(duck.philosophize)

  val frog2 = new Frog2
  println(frog2.philosophize)
