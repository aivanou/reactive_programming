package simulators.epidemy

class EpidemySimulator extends Simulator {

  val rooms: List[Room] = createRooms()
  val people: List[Person] = distribute()

  def distribute(): List[Person] = {
    def assign(id: Int): List[Person] = {
      if (id == 0)
        return List()
      val room = Util.oneOf(rooms)
      val person = new Person(id, room, HealthState.Healthy)
      room.addPerson(person)
      return person :: assign(id - 1)
    }
    assign(Util.config.population)
  }

  def assignInfected(): Unit = {
    val ninfected = Util.config.population * 50 / 100
    println("infected: " + ninfected)
    for (i <- 0 until ninfected) {
      val p = people(i)
      p.setHealthState(HealthState.Infected)
    }
  }

  def createRooms(): List[Room] = {
    var rms = List[Room]()
    for (x <- 0 until Util.config.MAX_ROW_ROOMS) {
      for (y <- 0 until Util.config.MAX_COL_ROOMS) {
        val room = new Room(Point(x, y))
        rms = room :: rms
      }
    }
    rms
  }

  def startSimulation(): Unit = {

    assignInfected()
    registerStates(people)
    registerMoveActions(people, 10)
    println("day: 1")
    runEventsUntilTime(1)
    println("after day 1: " + agenda.length)
    println("day: 2")
    runEventsUntilTime(2)
    println("after day 2: " + agenda.length)
    println("day: 3")
    runEventsUntilTime(3)
    println("after day 3: " + agenda.length)
  }

  def runEventsUntilTime(untilDay: Int): Unit = {
    if (day <= untilDay) {
      next
      runEventsUntilTime(untilDay)
    }
  }

  def registerStates(ppl: List[Person]): Unit = {
    for (p <- ppl) {
      logAction(p)
      incubation(p)
      infected(p)
      immune(p)
    }
  }

  def logAction(person: Person): Unit = {
    for (v <- HealthState.values) {
      person.addAction(v, () => {
        println("person " + person.id + " health state: " + v)
      })
    }
  }

  def registerMoveActions(ppl: List[Person], days: Int): Unit = {
    for (day <- 1 until days + 1) {
      registerMoveAction(ppl, day)
    }
  }

  def registerMoveAction(ppl: List[Person], day: Int): Unit = {
    for (p <- ppl) {
      registerMoveAction(p, day)
    }
  }

  def registerMoveAction(person: Person, day: Int) = {
    afterDelay(day) {
      if (Util.probability(20))
        tryMove(person)
      dayAction(person)
    }
  }

  def tryMove(person: Person): Unit = {
    val pt = person.getPosition.coordinates
    person.getPosition.removePerson(person)
    val newRoom = roomByCoords(rooms, newPosition(pt))
    person.setRoom(newRoom)
    newRoom.addPerson(person)
  }

  def newPosition(pt: Point): Point = {
    return pt
  }

  def roomByCoords(rooms: List[Room], pt: Point): Room = {
    for (room <- rooms)
      if (room.coordinates.equals(pt))
        return room
    return null
  }

  def dayAction(person: Person): Unit = {
    if (person.getPosition.hasInfectious && Util.probability(Util.config.TRANSMISS)) {
      person.setHealthState(HealthState.Incubation)
    }
  }

  def incubation(person: Person): Unit = {
    def incubationAction(): Unit = {
      afterDelay(6) {
        person.setHealthState(HealthState.Infected)
      }
    }
    person addAction(HealthState.Incubation, incubationAction)
  }

  def infected(person: Person): Unit = {
    def trigger(): Unit = {
      if (Util.probability(25))
        person.setHealthState(HealthState.Dead)
    }
    def infectedAction(): Unit = {
      afterDelay(8) {
        trigger()
      }
      afterDelay(9) {
        trigger()
      }
      afterDelay(10) {
        if (person.getHealthState != HealthState.Dead) {
          person.setHealthState(HealthState.Immune)
        }
      }
      afterDelay(11) {
        if (person.getHealthState != HealthState.Dead && person.getHealthState != HealthState.Immune) {
          person.setHealthState(HealthState.Immune)
        }
      }
    }
    person addAction(HealthState.Infected, infectedAction)
  }

  def immune(person: Person): Unit = {
    def immuneAction(): Unit = {
      afterDelay(2) {
        person.setHealthState(HealthState.Healthy)
      }
    }
    person addAction(HealthState.Immune, immuneAction)
  }

}

object Directions extends Enumeration {
  type Direction = Value
  val Left, Right, Up, Down, Place = Value
}
