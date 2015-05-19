package geo.structure

import geo.domain.GPoint

/**
 * @author Oliver Lea
 */
protected class QuadTreeNode[T](var x: Double,
                                var y: Double,
                                var width: Double,
                                var height: Double,
                                var parent: QuadTreeNode[T]) {

  var nw: Option[QuadTreeNode[T]] = None
  var sw: Option[QuadTreeNode[T]] = None
  var ne: Option[QuadTreeNode[T]] = None
  var se: Option[QuadTreeNode[T]] = None

  var elements = List[(GPoint, T)]()

  def withinBounds(p: GPoint): Boolean = {
    p.x >= x && p.y >= y && p.x <= (x + width) && p.y <= (y + height)
  }

  def nodeType: QuadTreeNodeType.Value = {
    if (presentChildNodes.nonEmpty) QuadTreeNodeType.REFERENCE
    else if (elements.isEmpty) QuadTreeNodeType.EMPTY
    else QuadTreeNodeType.LEAF
  }

  def split(): Unit = {
    val halfWidth: Double = width / 2
    val halfHeight: Double = height / 2
    val forNw = new QuadTreeNode[T](x, y, halfWidth, halfHeight, this)
    val forSw = new QuadTreeNode[T](x, y + halfHeight, halfWidth, halfHeight, this)
    val forNe = new QuadTreeNode[T](x + halfWidth, y, halfWidth, halfHeight, this)
    val forSe = new QuadTreeNode[T](x + halfWidth, y + halfHeight, halfWidth, halfHeight, this)

    for (e <- elements) {
      val p: GPoint = e._1
      if (forNw.withinBounds(p)) forNw.elements = e :: forNw.elements
      else if (forSw.withinBounds(p)) forSw.elements = e :: forSw.elements
      else if (forNe.withinBounds(p)) forNe.elements = e :: forNe.elements
      else forSe.elements = e :: forSe.elements
    }
    elements = List()
    nw = if (forNw.elements.nonEmpty) Some(forNw) else None
    sw = if (forSw.elements.nonEmpty) Some(forSw) else None
    ne = if (forNe.elements.nonEmpty) Some(forNe) else None
    se = if (forSe.elements.nonEmpty) Some(forSe) else None
  }

  def presentChildNodes: List[QuadTreeNode[T]] = {
    var l = if (nw.isDefined) List(nw.get) else List()
    l = if (sw.isDefined) sw.get :: l else l
    l = if (ne.isDefined) ne.get :: l else l
    l = if (se.isDefined) se.get :: l else l
    l
  }
}
