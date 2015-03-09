package quora

import collection.mutable.ListBuffer

object QuoraTypeheadSearch {


  implicit val ord = new Ordering[SearchObject] {
    override def compare(x: SearchObject, y: SearchObject): Int = {
      if (x.id.equals(y.id)) return 0
      else if (x.weigth == y.weigth) return 1
      else return x.weigth.compareTo(y.weigth)
    }
  }

  def main(args: Array[String]): Unit = {
    val trie = new Trie()
    trie.insert(SearchObject("id1", "how I met your mother", "user", 2.32f, 0))
    trie.insert(SearchObject("id2", "turn this black please", "user", 2.32f, 0))
    trie.insert(SearchObject("id3", "what to do with peter pan", "user", 2.32f, 0))
    trie.insert(SearchObject("id4", "not this is not black", "user", 2.32f, 0))
    trie.insert(SearchObject("id5", "how I met your mother", "user", 6.32f, 0))
    trie.insert(SearchObject("id6", "how I met your mother", "user", 8.32f, 0))
    trie.insert(SearchObject("id7", "how I met your mother", "user", 1.32f, 0))
    //    trie.printTree()
    val res = trie.query(2, "ho me")
    println(res)
    trie.delete("id5")
    println(trie.query(2, "ho me"))

  }

  case class T(l: ListBuffer[String])

  class Trie {

    import scala.collection.mutable._;

    implicit val ord = new Ordering[SearchObject] {
      override def compare(x: SearchObject, y: SearchObject): Int = {
        if (x.id.equals(y.id)) return 0
        else if (x.weigth == y.weigth) return 1
        else return -1 * x.weigth.compareTo(y.weigth)
      }
    }

    implicit val tupleOrd = new Ordering[(Int, SearchObject)] {
      override def compare(x: (Int, SearchObject), y: (Int, SearchObject)): Int = {
        return ord.compare(x._2, y._2)
      }
    }

    val topics: HashSet[String] = HashSet[String]()

    val root = TrieNode(' ', null, ListBuffer[TrieNode](),
      TreeSet[SearchObject](), ListBuffer[TreeSet[SearchObject]](),
      HashMap[String, ListBuffer[SearchObject]](), ListBuffer[HashMap[String, ListBuffer[SearchObject]]]())

    val entityTable: HashMap[String, SearchObject] = new HashMap[String, SearchObject]()

    def insert(word: String): TrieNode = {
      def insert(it: Iterator[Char], node: TrieNode): TrieNode = {
        if (!it.isEmpty) {
          insert(it, getOrCreateChild(it.next, node))
        }
        else {
          return node
        }
      }
      return insert(word.iterator, root)
    }

    def wquery(nresults: Int, text: String, boosting: ListBuffer[(String, Float)]): Set[String] = {
      val rawObjects: ListBuffer[SearchObject] = ListBuffer[SearchObject]()
      text.split(" +").foreach {
        word =>
          getNodeByToken(word.toLowerCase) match {
            case Some(node) => {
              for (sobj <- node.index) {
                rawObjects += sobj
              }
              for (chIndex <- node.childrenIndex) {
                for (sobj <- chIndex) {
                  rawObjects += sobj;
                }
              }
            }
          }
      }


      for ((topic, boost) <- boosting) {
        if (topics.contains(topic)) {

        }
      }

      val heap = PriorityQueue[(Int, SearchObject)]()
      for (i <- 0 until iteratorIndeces.size) {
        if (iteratorIndeces(i).hasNext) {
          val t = (i, iteratorIndeces(i).next)
          heap += t
        }
      }
      val results = HashSet[String]()
      while (results.size != nresults && !heap.isEmpty) {
        val (ind, query) = heap.dequeue
        val result = query.id
        results += result
        if (iteratorIndeces(ind).hasNext) {
          val t = (ind, iteratorIndeces(ind).next)
          heap += t
        }
      }
      return results
    }

    def query(nresults: Int, text: String): Set[String] = {
      val iteratorIndeces: ListBuffer[Iterator[SearchObject]] = ListBuffer[Iterator[SearchObject]]()
      text.split(" +").foreach {
        word =>
          getNodeByToken(word.toLowerCase) match {
            case Some(node) => {
              iteratorIndeces += node.index.iterator
              for (chIndex <- node.childrenIndex) {
                iteratorIndeces += chIndex.iterator
              }
            }
          }
      }
      val heap = PriorityQueue[(Int, SearchObject)]()
      for (i <- 0 until iteratorIndeces.size) {
        if (iteratorIndeces(i).hasNext) {
          val t = (i, iteratorIndeces(i).next)
          heap += t
        }
      }
      val results = HashSet[String]()
      while (results.size != nresults && !heap.isEmpty) {
        val (ind, query) = heap.dequeue
        val result = query.id
        results += result
        if (iteratorIndeces(ind).hasNext) {
          val t = (ind, iteratorIndeces(ind).next)
          heap += t
        }
      }
      return results
    }

    def getNodeByToken(token: String): Option[TrieNode] = {
      def getNodeByToken(it: Iterator[Char], node: TrieNode): Option[TrieNode] = {
        if (!it.isEmpty) {
          val letter = it.next
          val chNodes = node.children.filter(ch => ch.letter == letter)
          if (chNodes.isEmpty)
            return None
          getNodeByToken(it, chNodes.iterator.next)
        }
        else {
          return Some(node)
        }
      }
      return getNodeByToken(token.iterator, root)
    }

    def insert(sobject: SearchObject): Unit = {
      entityTable.put(sobject.id, sobject)
      topics += sobject.qtype
      sobject.body.split(" +").foreach {
        word =>
          val node = insert(word.toLowerCase)
          node.index += sobject
          if (!node.category.contains(sobject.qtype)) {
            node.category.put(sobject.qtype, ListBuffer[SearchObject]())
          }
          node.category.get(sobject.qtype).get += sobject
      }
    }

    def delete(id: String): Unit = {
      entityTable.get(id) match {
        case Some(query) =>
          delete(query)
        case _ => {}
      }
      entityTable.remove(id)
    }

    def delete(query: SearchObject): Unit = {
      query.body.split(" +").foreach {
        word =>
          val node = insert(word.toLowerCase)
          node.index.remove(query)
      }
    }

    def printTree(): Unit = {
      def printNode(node: TrieNode, depth: Int): Unit = {
        println(node.letter + "  " + node.childrenIndex + " : " + depth)
        if (!node.children.isEmpty) {
          for (ch <- node.children)
            printNode(ch, depth + 1)
        }
      }
      printNode(root, 0)
    }

    private def getOrCreateChild(letter: Char, node: TrieNode): TrieNode = {
      def insertEmptyStructure(node: TrieNode, emptyStructure: TreeSet[SearchObject], emptyCat: HashMap[String, ListBuffer[SearchObject]]): Unit = {
        if (node != null) {
          node.childrenIndex += emptyStructure
          node.childrenCategories += emptyCat
          insertEmptyStructure(node.parent, emptyStructure, emptyCat)
        }
      }
      val chNodes = node.children.filter(ch => ch.letter == letter)
      if (chNodes.isEmpty) {
        val emptyTree = new TreeSet[SearchObject]()
        val emptyCategory = HashMap[String, ListBuffer[SearchObject]]()
        val newNode = TrieNode(letter, node, ListBuffer[TrieNode](),
          emptyTree, ListBuffer[TreeSet[SearchObject]](),
          emptyCategory, ListBuffer[HashMap[String, ListBuffer[SearchObject]]]())
        node.children += newNode
        insertEmptyStructure(newNode.parent, emptyTree, emptyCategory)
        return newNode
      }
      return chNodes.iterator.next();
    }

    case class TrieNode(letter: Char, parent: TrieNode, children: ListBuffer[TrieNode],
                        index: TreeSet[SearchObject], childrenIndex: ListBuffer[TreeSet[SearchObject]],
                        category: HashMap[String, ListBuffer[SearchObject]],
                        childrenCategories: ListBuffer[HashMap[String, ListBuffer[SearchObject]]]);

  }

  case class SearchObject(id: String, body: String, qtype: String, weigth: Float, boost: Float) {

    override def equals(obj: scala.Any): Boolean = {
      if (obj == null)
        return false;
      return super.equals(obj)
    }

    override def hashCode(): Int = super.hashCode()
  }

}
