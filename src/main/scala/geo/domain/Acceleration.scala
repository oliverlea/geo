package geo.domain

/**
 * @author Oliver Lea
 */
class Acceleration(val dx: Double, val dy: Double) {
  def stationary = dx == 0 && dy == 0

  def +(da: Acceleration): Acceleration = new Acceleration(dx + da.dx, dy + da.dy)

  def -(da: Acceleration): Acceleration = new Acceleration(dx - da.dx, dy - da.dy)

  def *(f: Double): Acceleration = new Acceleration(dx * f, dy * f)
}
