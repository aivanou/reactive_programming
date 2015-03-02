package simulators.epidemy


object Main {

  def main(args: Array[String]): Unit = {
    val simulator = new EpidemySimulator()
    simulator.startSimulation()
  }

}
