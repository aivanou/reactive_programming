package simulators.curcuit

abstract class Simulation {

  type Action = () => Unit

  case class Event(time: Int, action: Action)

  private var currentTime = 0

  def time = currentTime

  private var agenda = List[Event]()


  def afterDelay(delay: Int)(block: => Unit): Unit = {
    val event = Event(delay + time, () => block)
    agenda = insert(event, agenda)
  }

  def run(): Unit = {
    afterDelay(0) {
      println("starting simulation; time: " + currentTime)
    }
    loop()
  }

  def loop(): Unit = {
    println(agenda.length)
    agenda match {
      case e :: tail =>
        agenda = tail
        e.action();
        currentTime = e.time
        loop()
      case _ => {}
    }
  }

  private def insert(event: Event, lst: List[Event]): List[Event] = lst match {
    case el :: tail if el.time < event.time => el :: insert(event, tail)
    case _ => event :: lst
  }
}
