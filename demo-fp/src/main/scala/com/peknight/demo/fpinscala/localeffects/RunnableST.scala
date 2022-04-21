package com.peknight.demo.fpinscala.localeffects

/**
 * 类型S我们并不关心具体是什么类型，使用这个特质隐藏掉类型S
 */
trait RunnableST[A]:
  def apply[S]: ST[S, A]
