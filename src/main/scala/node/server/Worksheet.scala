package node.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import rx.concurrency.NewThreadScheduler
import rx.subjects._

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.language.postfixOps

object Worksheet {

  implicit class FutureCompanionOps[T](val f: Future.type) extends AnyVal {

  }


  class MyHandler extends HttpHandler {
    def handle(t: HttpExchange): Unit = {
      for (k <- t.getRequestHeaders.keySet.toArray) {
        println(k + "  " + t.getRequestHeaders.get(k))
      }
      val response = "This is the response";
      t.sendResponseHeaders(200, response.length());
      val os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }


  def main(args: Array[String]): Unit = {
    //    val server = HttpServer.create(new InetSocketAddress(9900), 0)
    //    server.createContext("/test", new MyHandler());
    //    val executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue)
    //    server.setExecutor(executor)
    //    server.start();
    //    val p = Promise[String]()
    //    p.complete {
    //      Thread.sleep(1000);
    //      Success("yes")
    //    }
    //    println("no")

    val x = 4
    object Prime {
      def unapply(x: Int): Option[Int] = if ((2 until x).forall(x % _ != 0)) Some(x) else None
    }

    1 match {
      case Prime(x) => println("We like prime numbers!")
    }
    //    val ys = Observable[Observable[Int]] =
    //      xs.map(x => Observable.interval(Duration.create(x, TimeUnit.SECONDS)).map(_ => x).take(2))
    //    val bufs = ticks.buffer(5, 1)
    //    val s = bufs.subscribe(b => println(b))
    //    readLine()
    //    s.unsubscribe()
  }

}
