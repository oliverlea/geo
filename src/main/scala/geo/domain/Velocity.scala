package geo.domain

/**
 * @author Oliver Lea
 */
class Velocity(val dx: Double, val dy: Double) {
  def stationary = dx == 0 && dy == 0

  def heading = {
    val ddx = dx match {
      case x if dx > 0 => 1
      case x if dx < 0 => -1
      case _ => 0
    }
    val ddy = dy match {
      case y if dy > 0 => 1
      case y if dy < 0 => -1
      case _ => 0
    }
    new Velocity(ddx, ddy)
  }

  def +(da: Velocity): Velocity = new Velocity(dx + da.dx, dy + da.dy)

  def -(da: Velocity): Velocity = new Velocity(dx - da.dx, dy - da.dy)

  def *(f: Double): Velocity = new Velocity(dx * f, dy * f)

  override def toString = s"[dx: $dx; dy: $dy]"
}
