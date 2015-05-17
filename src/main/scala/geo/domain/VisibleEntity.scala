package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Oliver Lea
 */
abstract class VisibleEntity(private val gp: GeoPanel,
                             private val initialVelocity: Velocity,
                             private var _position: GPoint) extends Entity {
  def visible: Boolean = {
    _position.x >= 0 && _position.y >= 0 && _position.x <= gp.getWidth && _position.y <= gp.getHeight
  }

  def render(g: Graphics2D): Unit
}