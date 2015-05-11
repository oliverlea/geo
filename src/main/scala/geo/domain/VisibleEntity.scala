package geo.domain

import java.awt.Graphics

/**
 * @author Oliver Lea
 */
trait VisibleEntity extends Entity with Positionable {
  def render(g: Graphics): Unit
  def visible: Boolean = {
    position.x < 0 || position.y < 0 || position.x > 800 || position.y > 600
  }
}
