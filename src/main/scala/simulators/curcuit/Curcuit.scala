package simulators.curcuit

abstract class Curcuit extends Simulation {

  val invertDelay: Int
  val andDelay: Int
  val orDelay: Int

  def invert(in: Wire, out: Wire): Unit = {
    gate(in, out, invertDelay) {
      out setState !in.getState
    }
  }

  def andGate(in1: Wire, in2: Wire, out: Wire): Unit = {
    gate(in1, in2, out, andDelay) {
      out setState in1.getState & in2.getState
    }
  }

  def orGate(in1: Wire, in2: Wire, out: Wire): Unit = {
    gate(in1, in2, out, orDelay) {
      out setState in1.getState | in2.getState
    }
  }

  def probe(name: String, wire: Wire): Unit = {
    def probeAction(): Unit = {
      println(name + "  " + time + "  new state: " + wire.getState)
    }
    wire addAction probeAction
  }

  private def gate(in1: Wire, in2: Wire, out: Wire, delay: Int = 1)(action: => Unit): Unit = {
    def gateAction(): Unit = {
      afterDelay(delay) {
        action
      }
    }
    in1 addAction gateAction
    in2 addAction gateAction
  }

  private def gate(in: Wire, out: Wire, delay: Int)(action: => Unit): Unit = {
    def gateAction(): Unit = {
      afterDelay(delay) {
        action
      }
    }
    in addAction gateAction
  }

}
