package com.peknight.demo.scala3.traits

trait MyQueue2 extends BasicIntQueue, Incrementing, Doubling:

  // 基类的put方法被调用了"两次"
  override def put(x: Int) =
    // 很少有人这么用，这是合法的Scala代码
    super[Incrementing].put(x)
    super[Doubling].put(x)
