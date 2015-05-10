package geo.domain

/**
 * @author Oliver Lea
 */
class Velocity(val dx: Double, val dy: Double) {
  def stationary = dx == 0 && dy == 0

  def +(da: Velocity): Velocity = new Velocity(dx + da.dx, dy + da.dy)

  def -(da: Velocity): Velocity = new Velocity(dx - da.dx, dy - da.dy)

  def *(f: Double): Velocity = new Velocity(dx * f, dy * f)
}
