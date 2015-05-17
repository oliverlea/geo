package geo.domain

import java.awt.Point

import geo.domain.GPoint.SIMILARITY

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class GPoint(val x: Double, val y: Double) extends Serializable {

  def this(p: Point) = this(p.getX, p.getY)

  def +(p: (Double, Double)) = {
    new GPoint(x + p._1, y + p._2)
  }

  def +(a: Velocity) = new GPoint(x + a.dx, y + a.dy)

  def -(p: (Double, Double)) = new GPoint(x - p._1, y - p._2)

  def -(p: GPoint) = new GPoint(x - p.x, y - p.y)

  def -(a: Velocity) = new GPoint(x - a.dx, y - a.dy)

  def roundX: Int = math.round(x).toInt
  def roundY: Int = math.round(y).toInt

  def ~?(p: GPoint): Boolean = {
    math.abs(this.x - p.x) < SIMILARITY && math.abs(this.y - p.y) < SIMILARITY
  }

  override def toString: String = s"[$x, $y]"

  override def equals(o: Any): Boolean = o match {
    case o: GPoint => o.x == this.x && o.y == this.y
    case _ => false
  }
}

object GPoint {
  var SIMILARITY = 0.1
}
