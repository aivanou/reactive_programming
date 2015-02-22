package quickcheck

import org.scalacheck.Arbitrary._

object Main {

  def main(args: Array[String]): Unit = {
    println("nene")
    val a = for {
      x <- arbitrary[Int]
    } yield x
    println(a.sample.get)
    val ah = new QuickCheckHeap with BinomialHeap
    println(ah.arbHeap.arbitrary.sample.get)
    val b = (1 until 10)
    println(b)
  }


}
