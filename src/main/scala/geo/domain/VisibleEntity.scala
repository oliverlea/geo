package geo.domain

import java.awt.Graphics

/**
 * @author Oliver Lea
 */
trait VisibleEntity extends Positionable {

  def tick(): Unit
  def render(g: Graphics): Unit
}
