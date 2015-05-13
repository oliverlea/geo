package geo.domain

/**
 * @author Oliver Lea
 */
abstract class Entity {
  def tick(delta: Double): Unit
  def shouldLive: Boolean
}
