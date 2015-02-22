package simulators.curcuit

/**
 * Created by aliaksandr on 2/21/15.
 */
class Wire {

  type Action = () => Unit

  private var actions: List[Action] = List()

  private var state: Boolean = false


  def getState = state

  def setState(st: Boolean): Unit = {
    if (st == state)
      return
    state = st
    actions.foreach(_())
  }

  def addAction(a: Action): Unit = {
    actions = a :: actions
    a()
  }

}
