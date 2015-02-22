package simulators.epidemy

abstract class Simulator {

  type Action = () => Unit

  case class Event(day: Int, action: Action)

  private var currentDay = 1

  def day = currentDay

  private var agenda: List[Event] = List()

  def afterDelay(delay: Int)(action: => Unit): Unit = {
    val event = Event(day + delay, () => action)
    def insert(ag: List[Event]): List[Event] = {
      if (ag.isEmpty || event.day < ag.head.day) event :: ag
      else ag.head :: insert(ag.tail)
    }
    agenda = insert(agenda)
  }

  def next(): Unit = {
    agenda match {
      case List() => {}
      case Event(time, action) :: tail =>
        agenda = tail
        action()
        currentDay = time
    }
  }

  def run(): Unit = {
    def run(ag: List[Event]): Unit = {
      ag match {
        case List() => {}
        case _ =>
          next
          run(ag)
      }
    }
  }


}
