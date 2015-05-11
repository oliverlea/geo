package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class Bullet(private val gp: GeoPanel, var position: GPoint, var heading: Velocity) extends VisibleEntity {

  private val velocity = new Velocity(
    heading.dx * Bullet.VELOCITY_FACTOR,
    heading.dy * Bullet.VELOCITY_FACTOR
  )

  override def render(g: Graphics2D): Unit = {
    g.drawOval(
      math.round(position.x).toInt,
      math.round(position.y).toInt,
      Bullet.SIZE,
      Bullet.SIZE
    )
  }

  override def tick(delta: Double): Unit = {
    position += velocity * delta
  }
}

object Bullet {
  val VELOCITY_FACTOR = 10
  val SIZE = 3
}
