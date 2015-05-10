package geo.domain

/**
 * @author Oliver Lea
 */
trait Entity {
  def tick(delta: Double): Unit
}
