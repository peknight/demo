package com.peknight.demo.scala3.mutableobjects

abstract class Simulation:
  type Action = () => Unit
  case class WorkItem(time: Int, action: Action)
  private var curtime = 0
  def currentTime: Int = curtime
  private var agenda: List[WorkItem] = List()
  private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] =
    if ag.isEmpty || item.time < ag.head.time then item :: ag else ag.head :: insert(ag.tail, item)

  def afterDelay(delay: Int)(block: => Unit) =
    val item = WorkItem(currentTime + delay, () => block)
    agenda = insert(agenda, item)

  private def next() =
    (agenda: @unchecked) match
      case item :: rest =>
        agenda = rest
        curtime = item.time
        item.action()

  def run() =
    afterDelay(0) {
      println(s"*** simulation started, time = $currentTime ***")
    }
    while agenda.nonEmpty do next()