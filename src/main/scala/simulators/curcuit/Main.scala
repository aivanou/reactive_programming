package simulators.curcuit

object Main extends Curcuit {

  def main(args: Array[String]): Unit = {
    val in1 = new Wire()
    val in2 = new Wire()
    val out1 = new Wire()
    invert(in1, out1)
    //    orGate(in1, in2, out1)
    probe("input1", in1)
    probe("input2", in2)
    probe("outpup1", out1)

    run()
  }


  override val invertDelay: Int = 1
  override val andDelay: Int = 2
  override val orDelay: Int = 3
}
