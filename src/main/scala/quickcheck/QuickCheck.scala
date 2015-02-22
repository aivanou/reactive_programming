package quickcheck

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("min2") = forAll { heap: H =>
    findMin(insert(Int.MinValue, heap)) == Int.MinValue
  }

  property("insert1") = forAll { a: Int =>
    val h = insert(a, empty)
    !isEmpty(h)
  }

  property("deleteMin1") = forAll { heap: H =>
    val h = deleteMin(insert(Int.MinValue, heap))
    findMin(h) != Int.MinValue
  }

  lazy val genHeap: Gen[H] =
    for {
      test <- arbitrary[Int]
      v <- arbitrary[Int]
      next <- if (test % 3 == 0) genEmptyHeap else genHeap
    } yield insert(v, next)


  lazy val genEmptyHeap: Gen[H] =
    for {
      v <- arbitrary[Int]
    } yield empty

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

}
