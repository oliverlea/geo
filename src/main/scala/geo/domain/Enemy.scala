package geo.domain

import java.awt.Graphics2D

import geo.GeoPanel
import geo.domain.Enemy.SIZE

/**
 * @author Oliver Lea
 */
class Enemy(private val gp: GeoPanel,
            private var velocity: Velocity,
            private var position: GPoint) extends VisibleEntity(gp, velocity, position) {

  override def render(g: Graphics2D): Unit = {
    g.drawOval(
      position.roundX - SIZE / 2,
      position.roundY - SIZE / 2,
      SIZE,
      SIZE
    )
  }

  override def tick(delta: Double): Unit = {
    position = nextPosition(position, velocity)
  }

  private def nextPosition(p: GPoint, v: Velocity): GPoint = p + v

  override def shouldLive: Boolean = visible
}

object Enemy {
  val SIZE: Int = 10
  val SPEED: Double = 5
}
