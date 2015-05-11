package geo.domain

import java.awt.Graphics

import geo.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class Bullet(private val gp: GeoPanel, var position: GPoint, var heading: Velocity) extends VisibleEntity {

  private val velocity = new Velocity(
    heading.dx * Bullet.VELOCITY_FACTOR,
    heading.dy * Bullet.VELOCITY_FACTOR
  )

  override def render(g: Graphics): Unit = {
    g.drawLine(
      math.round(position.x).toInt,
      math.round(position.y).toInt,
      math.round(position.y + 5).toInt,
      math.round(position.y + 5).toInt
    )
  }

  override def tick(delta: Double): Unit = {
    position += velocity * delta
  }
}

object Bullet {
  val VELOCITY_FACTOR = 5
}
