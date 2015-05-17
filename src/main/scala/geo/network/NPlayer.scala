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
 * @param _position
 */
class NPlayer(private val gp: GeoPanel,
              private val initialVelocity: Velocity,
              private var _position: GPoint) extends VisibleEntity(gp, initialVelocity, _position) with Serializable {

  def position = _position

  def position_=(p: GPoint): Unit = _position = p

  override def render(g: Graphics2D): Unit = {
    g.drawRect(_position.roundX - SIZE / 10, _position.roundY - SIZE / 10, SIZE, SIZE)
  }

  override def tick(delta: Double): Unit = {
  }

  override def shouldLive: Boolean = true

  override def toString: String = _position.toString
}

object NPlayer {
  val SIZE = 25
}