package simulators.epidemy

class EpidemySimulator extends Simulator {

  val people: List[Person] = distribute()
  val rooms: List[Room] = createRooms()

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
    val ninfected = Util.config.population * Util.config.PREVALENCE_RATE / 100
    for (_ <- 0 until ninfected) {
      val p = Util.oneOf(people)
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

  def tryMoveAction(person: Person) = {
    afterDelay(0) {
      if (Util.probability(20))
        move(person)
    }
  }

  def move(person: Person): Unit = {
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

  def healthy(person: Person): Unit = {
    def dayAction(): Unit = {
      afterDelay(1) {
        if (person.getPosition.hasInfectious && Util.probability(Util.config.TRANSMISS)) {
          person.setHealthState(HealthState.Incubation)
        }
      }
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
