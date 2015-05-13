package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class Bullet(private val gp: GeoPanel,
             private val velocity: Velocity,
             private var position: GPoint) extends VisibleEntity(gp, velocity, position) {

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
  val SPEED = 7.0
  val SIZE = 3
}
