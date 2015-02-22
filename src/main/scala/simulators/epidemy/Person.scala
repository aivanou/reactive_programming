package simulators.epidemy

case class Point(x: Int, y: Int)

object HealthState extends Enumeration {
  type State = Value
  val Healthy, Infected, Incubation, Immune, Dead = Value
}

class Person(pid: Int, rm: Room, state: HealthState.State) {

  type Action = () => Unit

  val id = pid

  private var room: Room = rm

  private var hstate = state
  private var sickDays = 0

  def getDaysBeenSick = sickDays

  private var roomChangedEvents: List[Action] = List()
  private var healthChangedEvents: Map[HealthState.State, List[Action]] = Map()

  def getPosition: Room = room

  def getHealthState: HealthState.State = hstate

  def setRoom(newRoom: Room): Unit = {
    if (newRoom.equals(room))
      return
    room = newRoom
    roomChangedEvents.foreach(_())
  }

  def setHealthState(st: HealthState.State): Unit = {
    if (st == hstate)
      return
    healthChangedEvents(st).foreach(_())
    hstate = st
  }

  def addAction(st: HealthState.State, action: Action): Unit = {
    val acts: List[Action] = action :: healthChangedEvents(st)
    healthChangedEvents = healthChangedEvents + (st -> acts)
  }
}
