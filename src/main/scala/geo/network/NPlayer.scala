package geo.network

import java.awt.Graphics2D

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
              private var position: GPoint) extends VisibleEntity(gp, initialVelocity, position) with Serializable {

  override def render(g: Graphics2D): Unit = {
    g.drawRect(position.roundX - SIZE / 10, position.roundY - SIZE / 10, SIZE, SIZE)
  }

  override def tick(delta: Double): Unit = {
  }

  override def shouldLive: Boolean = true

  override def toString: String = position.toString
}

object NPlayer {
  val SIZE = 10
}