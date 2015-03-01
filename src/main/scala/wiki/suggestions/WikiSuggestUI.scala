package wiki.suggestions


import scala.swing.event._
import scala.collection.mutable.ListBuffer
import scala.swing._
import swing.Swing._
import Orientation._


object WikiSuggestUI extends SimpleSwingApplication {

  implicit class ButtonOps(button: Button) {

    /** Returns a stream of button clicks.
      *
      * @param field the button
      * @return an observable with a stream of buttons that have been clicked
      */

    var lst = ListBuffer[Button]()

    def clicks: ListBuffer[Button] = {
      lst.insert(1, button)
      println("anen")
      return lst
    }

  }

  def top = new MainFrame {
    title = "Query wikipedia"
    minimumSize = new Dimension(900, 600)

    val searchTermField = new TextField
    val suggestionList = new ListView(ListBuffer[String]())
    val status = new Label(" ")
    val editorpane = new EditorPane {

      import javax.swing.border._

      border = new EtchedBorder(EtchedBorder.LOWERED)
      editable = false
      peer.setContentType("text/html")
    }

    val button = new Button("Get") {
    }

    val label = new Label {
      text = "No button clicks registered"
    }

    contents = new BoxPanel(orientation = Vertical) {
      contents += label
      border = EmptyBorder(top = 5, left = 5, bottom = 5, right = 5)
      contents += new BoxPanel(orientation = Horizontal) {
        contents += new BoxPanel(orientation = Vertical) {
          maximumSize = new Dimension(240, 900)
          border = EmptyBorder(top = 10, left = 10, bottom = 10, right = 10)
          contents += new BoxPanel(orientation = Horizontal) {
            maximumSize = new Dimension(640, 30)
            border = EmptyBorder(top = 5, left = 0, bottom = 5, right = 0)
            contents += searchTermField
          }
          contents += new ScrollPane(suggestionList)
          contents += new BorderPanel {
            maximumSize = new Dimension(640, 30)
            add(button, BorderPanel.Position.Center)
          }
        }
        contents += new ScrollPane(editorpane)
      }
      contents += status
    }
    var nClicks = 0
    printOutTextFieldChanges(searchTermField)
    type Button = scala.swing.Button

    type ButtonClicked = scala.swing.event.ButtonClicked

    object ButtonClicked {
      def unapply(x: Event) = x match {
        case bc: ButtonClicked => {
          nClicks += 1
          label.text = "Number of button clicks: " + nClicks
          Some(bc.source.asInstanceOf[Button])
        }
        case _ => None
      }
    }

    val eventScheduler = Nil
  }

  def printOutTextFieldChanges(field: TextField) = field subscribe {
    case ValueChanged(tf) => println(field.text)
  }
}
