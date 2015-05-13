package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Oliver Lea
 */
abstract class VisibleEntity(private val gp: GeoPanel,
                             private val initialVelocity: Velocity,
                             private var position: GPoint) extends Entity {
  def visible: Boolean = {
    position.x >= 0 && position.y >= 0 && position.x <= gp.getWidth && position.y <= gp.getHeight
  }
  def render(g: Graphics2D): Unit
}
