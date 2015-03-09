package wiki.suggestions


import java.util.PriorityQueue
import java.util.Scanner

import rx.concurrency.NewThreadScheduler
import rx.lang.scala._

import collection.immutable.HashMap
import scala.swing.event._
import scala.collection.mutable.ListBuffer
import scala.swing._
import swing.Swing._
import Orientation._
import scala.concurrent.ExecutionContext.Implicits.global

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

    val searchTerms: Observable[String] = Observable {
      observer => {
        searchTermField subscribe {
          case ValueChanged(tf) => observer.onNext(searchTermField.text)
          case _ => {}
        }
        Subscription {}
      }
    }

    val suggestions: Observable[List[AnyRef]] = Observable {
      observer => {
        searchTerms.subscribe(
          value => ObservableExt(WikiSearch.wikiSuggestions(value)).subscribe(v => observer.onNext(v)),
          error => ObservableExt(WikiSearch.wikiSuggestions("error")),
          () => observer.onCompleted()
        )
      }
    }

    val sch = Scheduler(NewThreadScheduler.getInstance())

    val suggestionsSubscr: Subscription = suggestions.observeOn(sch) subscribe {
      lst => {
        val res = for (value <- lst) yield value.toString
        suggestionList.listData = res
      }
    }

//    val selectedPage: Observable[String] = Observable {
//      observer => {
//        button.subscribe {
//          case ButtonClicked => observer.onNext(suggestionList.selection.items(0))
//        }
//        Subscription {}
//      }
//    }

//    val suggestedPage: Observable[String] = Observable {
//      observer => {
//        selectedPage.subscribe(
//          value => ObservableExt(WikiSearch.wikiPage(value)).subscribe(v => observer.onNext(v)),
//          error => observer.onNext("Error"),
//          () => observer.onCompleted()
//        )
//      }
//    }
  }

}
