package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class Bullet(private val gp: GeoPanel,
             var position: GPoint,
             var direction: Velocity) extends VisibleEntity(gp) {

  private val velocity = new Velocity(
    direction.dx * Bullet.VELOCITY_FACTOR,
    direction.dy * Bullet.VELOCITY_FACTOR
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
