package geo.domain

import java.awt.{Graphics2D, Rectangle}

import geo.domain.Bullet.SIZE
import geo.screens.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class Bullet(private val gp: GeoPanel,
             private val velocity: Velocity,
             var position: GPoint) extends VisibleEntity(gp, velocity) {


  override def tick(delta: Double): Unit = {
    position = nextPosition(position, velocity * delta)
  }

  private def nextPosition(p: GPoint, v: Velocity): GPoint = {
    p + v
  }

  override def shouldLive: Boolean = visible

  override def render(g: Graphics2D): Unit = {
    val b = bounds
    g.drawOval(
      b.x,
      b.y,
      b.width,
      b.height
    )
  }

  override def bounds = new Rectangle(
    position.roundX - SIZE / 2,
    position.roundY - SIZE / 2,
    SIZE,
    SIZE
  )

  override def collidedWith(ve: VisibleEntity): Unit = ()
}

object Bullet {
  val SPEED: Double = 7
  val SIZE: Int = 3
}
