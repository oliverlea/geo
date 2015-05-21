package geo.network

import java.awt.{Graphics2D, Rectangle}

import geo.GeoPanel
import geo.domain.{GPoint, Velocity, VisibleEntity}
import geo.network.NPlayer.SIZE

/**
 * Network Player – represents a player from over the network
 *
 * @author Paulius Imbrasas
 * @param gp
 * @param initialVelocity
 * @param position
 */
class NPlayer(private val gp: GeoPanel,
              private val initialVelocity: Velocity,
              var position: GPoint) extends VisibleEntity(gp, initialVelocity) with Serializable {

  override def render(g: Graphics2D): Unit = {
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height)
  }

  override def tick(delta: Double): Unit = {
  }

  override def shouldLive: Boolean = true

  override def bounds = new Rectangle(position.roundX - SIZE / 10, position.roundY - SIZE / 10, SIZE, SIZE)

  override def collidedWith(ve: VisibleEntity): Unit = {}

  override def toString: String = position.toString
}

object NPlayer {
  val SIZE = 20
}