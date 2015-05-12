package geo.domain

/**
 * @author Oliver Lea
 */
class Velocity(val dx: Double, val dy: Double) {
  def stationary = (dx < 1E-5 && dx > -1E-5) && (dy < 1E-5 && dy > -1E-5)
  def stationaryInDirection(dir: Direction.Value) = dir match {
    case Direction.UP => dy > -1E-5
    case Direction.DOWN => dy < 1E-5
    case Direction.LEFT => dx > -1E-5
    case Direction.RIGHT => dx < 1E-5
  }

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

  def scaleAccelerate(dir: Direction.Value, factor: Double): Velocity = {
    val newX: Double = if (dir != Direction.LEFT && dir != Direction.RIGHT) dx else dx match {
      case _ if dx > 0 => if ((dx * factor) > 0) dx * factor else 0
      case _ if dx < 0 => if ((dx * factor) < 0) dx * factor else 0
      case _ => dx
    }
    val newY: Double = if (dir != Direction.UP && dir != Direction.DOWN) dy else dy match {
      case _ if dy > 0 => if ((dy * factor) > 0) dy * factor else 0
      case _ if dy < 0 => if ((dy * factor) < 0) dy * factor else 0
      case _ => dy
    }
    new Velocity(newX, newY)
  }

  def linearAccelerate(direction: Direction.Value, toAddVel: Double): Velocity = {
    direction match {
      case Direction.UP => new Velocity(dx, dy - toAddVel)
      case Direction.DOWN => new Velocity(dx, dy + toAddVel)
      case Direction.LEFT => new Velocity(dx - toAddVel, dy)
      case Direction.RIGHT => new Velocity(dx + toAddVel, dy)
    }
  }

  def *(f: (Double, Double)): Velocity = new Velocity(dx * f._1, dy * f._2)

  override def toString = s"[dx: $dx; dy: $dy]"
}
