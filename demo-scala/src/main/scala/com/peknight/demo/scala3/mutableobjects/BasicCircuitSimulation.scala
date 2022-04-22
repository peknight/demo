package com.peknight.demo.scala3.mutableobjects

abstract class BasicCircuitSimulation extends Simulation:

  def InverterDelay: Int
  def AndGateDelay: Int
  def OrGateDelay: Int

  class Wire:
    private var sigVal = false
    private var actions: List[Action] = List.empty

    def getSignal = sigVal
    def setSignal(s: Boolean) =
      if s != sigVal then
        sigVal = s
        actions.foreach(_())

    def addAction(a: Action) =
      actions = a :: actions
      a()

  def inverter(input: Wire, output: Wire): Unit =
    def invertAction() =
      val inputSig = input.getSignal
      afterDelay(InverterDelay) {
        output setSignal !inputSig
      }
    input addAction invertAction

  def andGate(a1: Wire, a2: Wire, output: Wire): Unit =
    def andAction() =
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (a1Sig & a2Sig)
      }
    a1 addAction andAction
    a2 addAction andAction

  def orGate(o1: Wire, o2: Wire, output: Wire): Unit =
    def orAction() =
      val o1Sig = o1.getSignal
      val o2Sig = o2.getSignal
      afterDelay(OrGateDelay) {
        output setSignal (o1Sig | o2Sig)
      }
    o1 addAction orAction
    o2 addAction orAction

  def probe(name: String, wire: Wire) =
    def probeAction() =
      println(s"$name $currentTime new-value = ${wire.getSignal}")
    wire addAction probeAction
