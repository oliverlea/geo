package geo.structure

import geo.domain.GPoint

/**
 * @author Oliver Lea
 */
class QuadTree[T](minX: Double, minY: Double, maxX: Double, maxY: Double) {

  private val root = new QuadTreeNode[T](minX, minY, maxX, maxY, null)
  private var count = 1

  def set(p: GPoint, t: T): Unit = {
    if (p.x >= minX && p.y >= minY && p.x <= maxX && p.y <= maxY) {
      if (insert(root, p, t))
        count += 4
    } else throw new IllegalArgumentException(s"$p outside the bounds of the quadtree: $this")
  }

  def getElements: List[List[T]] = {
    getLeaves.map(qtn => qtn.elements.map(_._2))
  }

  def getLeaves: List[QuadTreeNode[T]] = {
    def getLeavesR(l: List[QuadTreeNode[T]], node: QuadTreeNode[T]): List[QuadTreeNode[T]] = {
      val nodeType = node.nodeType
      nodeType match {
        case QuadTreeNodeType.EMPTY => l
        case QuadTreeNodeType.LEAF => node :: l
        case _ =>
          var newL = l
          for (n <- node.presentChildNodes) {
            newL = getLeavesR(newL, n)
          }
          newL
      }
    }
    getLeavesR(List(), root)
  }

  private def insert(node: QuadTreeNode[T], p: GPoint, t: T): Boolean = {
    def insertR(node: QuadTreeNode[T], p: GPoint, t: T, split: Boolean): Boolean = {
      val nodeType = node.nodeType
      var newSplit = false
      if (nodeType == QuadTreeNodeType.LEAF || nodeType == QuadTreeNodeType.EMPTY) {
        if (node.elements.size < QuadTree.ELEMENTS_PER_LEAF) {
          node.elements = (p, t) :: node.elements
          return split
        } else {
          node.split()
          newSplit = true
        }
      }
      val quadrantNode = determineQuadrant(node, p)
      if (quadrantNode.isDefined)
        insertR(quadrantNode.get, p, t, newSplit)
      else
        newSplit
    }
    insertR(node, p, t, split = false)
  }

  private def determineQuadrant(parentNode: QuadTreeNode[T], p: GPoint): Option[QuadTreeNode[T]] = {
    for (n <- parentNode.presentChildNodes) {
      if (n.withinBounds(p))
        return Some(n)
    }
    None
  }

  override def toString = s"QuadTree: {minX=$minX, minY=$minY, maxX=$maxX, mayY=$maxY}"
}

object QuadTree {
  val ELEMENTS_PER_LEAF = 10
}
