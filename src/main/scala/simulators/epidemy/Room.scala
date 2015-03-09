package simulators.epidemy

class Room(cr: Point) {

  var people: List[Person] = List()

  val coordinates: Point = cr

  def addPerson(person: Person): Unit = {
    people = person :: people
  }

  def removePerson(person: Person): Unit = {
    def remove(lst: List[Person]): List[Person] = lst match {
      case List() => List()
      case el :: tail if (el.id == person.id) => tail
      case el :: tail => el :: remove(tail)
    }
    people = remove(people)
  }

  def hasInfectious: Boolean = {
    for (p <- people)
      if (p.getHealthState == HealthState.Infected)
        return true
    return false
  }

  override def toString = {
    "room: [" + cr.x + ":" + cr.y + "]: " + people.length + "  "
  }
}