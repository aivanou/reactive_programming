package actors.webclient

import java.util.concurrent.Executor

import akka.actor.Actor

import concurrent.ExecutionContext

class Getter(url: String, depth: Int) extends Actor {

  implicit val exec = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  override def receive: Receive = {
    case body: String => {

    }
  }
}
