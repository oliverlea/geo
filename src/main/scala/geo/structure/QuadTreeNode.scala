package geo.structure

import geo.domain.{VisibleEntity, GPoint}

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
  def presentChildNodes: List[QuadTreeNode[T]] = {
    var l = if (nw.isDefined) List(nw.get) else List()
    l = if (sw.isDefined) sw.get :: l else l
    l = if (ne.isDefined) ne.get :: l else l
    l = if (se.isDefined) se.get :: l else l
    l
  }

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
    nw = Some(new QuadTreeNode[T](x, y, halfWidth, halfHeight, this))
    sw = Some(new QuadTreeNode[T](x, y + halfHeight, halfWidth, halfHeight, this))
    ne = Some(new QuadTreeNode[T](x + halfWidth, y, halfWidth, halfHeight, this))
    se = Some(new QuadTreeNode[T](x + halfWidth, y + halfHeight, halfWidth, halfHeight, this))

    for (e <- elements) {
      val p: GPoint = e._1
      if (nw.get.withinBounds(p)) nw.get.elements = e :: nw.get.elements
      else if (sw.get.withinBounds(p)) sw.get.elements = e :: sw.get.elements
      else if (ne.get.withinBounds(p)) ne.get.elements = e :: ne.get.elements
      else se.get.elements = e :: se.get.elements
    }
    elements = List()
  }
}
