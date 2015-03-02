package node

import com.sun.net.httpserver.HttpExchange

import scala.concurrent.{Future, Promise}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global


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
//  def main(args: Array[String]): Unit = {
//    val lst = List[Int](1, 2, 3)
//    lst.foldRight(10)((a, b) => b)
//  }

}

