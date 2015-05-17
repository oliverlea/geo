package geo.structure

import geo.domain.{VisibleEntity, GPoint}

/**
 * @author Oliver Lea
 */
protected class QuadTreeNode[T <: VisibleEntity](var x: Double,
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
    if (nw.isDefined || sw.isDefined || ne.isDefined || se.isDefined) QuadTreeNodeType.REFERENCE
    else if (elements.isEmpty) QuadTreeNodeType.EMPTY
    else QuadTreeNodeType.LEAF
  }

  def split(): Unit = {
    val halfWidth: Double = (x + width) / 2
    val halfHeight: Double = (y + height) / 2
    nw = Some(new QuadTreeNode[T](0, y + halfHeight, halfWidth, halfHeight, this))
    sw = Some(new QuadTreeNode[T](0, 0, halfWidth, halfHeight, this))
    ne = Some(new QuadTreeNode[T](x + halfWidth, halfHeight, halfWidth, halfHeight, this))
    se = Some(new QuadTreeNode[T](x + halfWidth, 0, halfWidth, halfHeight, this))
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
