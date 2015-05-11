package geo.domain

/**
 * @author Oliver Lea
 */
class Velocity(val dx: Double, val dy: Double) {
  def stationary = dx < 1E-5 && dy < 1E-5

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
  
  def *(f: Double, d: Direction.Value): Velocity = d match {
    case Direction.UP | Direction.DOWN => new Velocity(dx, dy * f)
    case Direction.LEFT | Direction.RIGHT => new Velocity(dx * f, dy)
  }

  def *(f: (Double, Double)): Velocity = new Velocity(dx * f._1, dy * f._2)

  override def toString = s"[dx: $dx; dy: $dy]"
}
