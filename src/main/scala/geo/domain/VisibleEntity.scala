package geo.domain

import java.awt.Graphics

/**
 * @author Oliver Lea
 */
trait VisibleEntity extends Entity with Positionable {
  def render(g: Graphics): Unit
}
