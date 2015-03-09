package node

import java.util.Comparator
import java.util.PriorityQueue

import akka.actor.Props

import scala.language.postfixOps


object Main {

  //  implicit class FutureCompanionOps[T](f: Future[T]) extends AnyVal {
  //    def userInput(message: String): Future[String] = Future {
  //      readLine(message)
  //
  //    }
  //
  //    def ensure[S](that: Future[S]): Future[T] = {
  //      val p = Promise[T]
  //
  //      f onComplete {
  //        case tryValue =>
  //          that onComplete {
  //            case Success(_) => p.complete(tryValue)
  //            case Failure(ex) => p.failure(ex)
  //          }
  //      }
  //
  //      return p.future
  //    }
  //
  //    def always(value: T): Future[T] = {
  //      val p = Promise[T]()
  //      f onComplete {
  //        case _ => p.complete(Success(value))
  //      }
  //      return p.future
  //    }
  //
  //    def all(fs: List[Future[T]]): Future[List[T]] = {
  //      val p = Promise[List[T]]()
  //      return fs.foldRight(p.future)((value, acc) => for {x <- value; xs <- acc} yield x :: xs)
  //    }
  //
  //  }
  //
  //  class Adventure {
  //    def collectCoins(): Try[Int] = {
  //      return Failure(new Exception("tt"))
  //    }
  //
  //    def buy(coins: Int): Try[String] = {
  //      return Success("yes")
  //    }
  //  }
  //

  class Tp {
    var v: Int = 10;
  }

  def main(args: Array[String]): Unit = {
    val q = new PriorityQueue[Tp](100, new Comparator[Tp] {
      override def compare(o1: Tp, o2: Tp): Int = return o1.v.compareTo(o2.v)
    })
    val t1 = new Tp
    val t2 = new Tp
    t1.v=20
    t2.v=10
    q.add(t1);
    q.remove(t2)
    println(q.poll().v)

  }


}

