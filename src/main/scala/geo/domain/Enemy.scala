package geo.domain

import java.awt.{Rectangle, Graphics2D}

import geo.GeoPanel
import geo.domain.Enemy.SIZE

/**
 * @author Oliver Lea
 */
class Enemy(private val gp: GeoPanel,
            private var velocity: Velocity,
            var position: GPoint) extends VisibleEntity(gp, velocity) {

  private var alive = true

  override def render(g: Graphics2D): Unit = {
    val b = bounds
    g.drawOval(
      b.x,
      b.y,
      b.width,
      b.height
    )
  }

  override def tick(delta: Double): Unit = {
    position = nextPosition(position, velocity)
  }

  private def nextPosition(p: GPoint, v: Velocity): GPoint = p + v

  override def shouldLive: Boolean = alive && visible

  override def bounds = new Rectangle(
    position.roundX - SIZE / 2,
    position.roundY - SIZE / 2,
    SIZE,
    SIZE
  )

  override def collidedWith(ve: VisibleEntity): Unit = {
    ve match {
      case ve: Bullet => alive = false
      case _ =>
    }
  }
}

object Enemy {
  val SIZE: Int = 10
  val SPEED: Double = 5
}
