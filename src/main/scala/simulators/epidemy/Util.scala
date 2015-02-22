package simulators.epidemy

import scala.util.Random

object Util {

  object config {
    val MAX_PEOPLE_IN_ROOM = 10
    val MAX_COL_ROOMS = 8
    val MAX_ROW_ROOMS = 8
    val population = 300
    // in percentage
    val PREVALENCE_RATE = 1
    val TRANSMISS = 40
  }


  def probability(percent: Int): Boolean = {
    val r = Random
    r.nextInt(100) <= percent
  }

  def oneOf[T](lst: List[T]): T = {
    val max = lst.length
    lst(Random.nextInt(max))
  }

}
