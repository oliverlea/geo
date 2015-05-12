package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Oliver Lea
 */
abstract class VisibleEntity(private val gp: GeoPanel) extends Entity with Positionable {
  def render(g: Graphics2D): Unit
  def visible: Boolean = {
    position.x < 0 || position.y < 0 || position.x > gp.getWidth || position.y > gp.getHeight
  }
}
