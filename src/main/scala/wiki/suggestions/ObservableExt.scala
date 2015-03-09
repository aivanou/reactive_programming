package wiki.suggestions


import rx.lang.scala.subjects.ReplaySubject

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Success
import rx.lang.scala.Observable
import util.Failure

object ObservableExt {

  /** Returns an observable stream of values produced by the given future.
    * If the future fails, the observable will fail as well.
    *
    * @param f future whose values end up in the resulting observable
    * @return an observable completed after producing the value of the future, or with an exception
    */
  def apply[T](f: Future[T])(implicit execContext: ExecutionContext): Observable[T] = {
    val subject = ReplaySubject[T]()
    f onComplete {
      case Success(value) => {
        subject.onNext(value);
        subject.onCompleted()
      }
      case Failure(ex) => {
        subject.onError(ex)
      }
    }
    return subject
  }

}