package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class Bullet(private val gp: GeoPanel,
             private val velocity: Velocity,
             private var position: GPoint) extends VisibleEntity(gp, velocity, position) {


  override def tick(delta: Double): Unit = {
    position = nextPosition(position, velocity * delta)
  }

  private def nextPosition(p: GPoint, v: Velocity): GPoint = {
    p + v
  }

  override def shouldLive: Boolean = visible

  override def render(g: Graphics2D): Unit = {
    g.drawOval(
      math.round(position.x).toInt,
      math.round(position.y).toInt,
      Bullet.SIZE,
      Bullet.SIZE
    )
  }
}

object Bullet {
  val SPEED: Double = 7
  val SIZE: Int = 3
}
