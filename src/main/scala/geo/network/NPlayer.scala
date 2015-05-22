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

  private var _velocity = new Velocity(initialVelocity)

  def velocity = _velocity
  def velocity_=(v: Velocity) = _velocity = v

  override def render(g: Graphics2D): Unit = {
    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height)
  }

  override def tick(delta: Double): Unit = {
    position = nextPosition(position, _velocity)
  }

  private def nextPosition(p: GPoint, v: Velocity): GPoint = {
    def wrap(x: Double, bound: Double): Double = x match {
      case y if x < 0 => bound - y
      case y if x > bound => y - bound
      case y => y
    }
    val maybeNextP = p + v
    val windowWidth = gp.getWidth.toDouble
    val windowHeight = gp.getHeight.toDouble
    if (maybeNextP.x >= 0 && maybeNextP.y >= 0 && maybeNextP.x <= windowWidth && maybeNextP.y <= windowHeight)
      maybeNextP
    else
      new GPoint(wrap(maybeNextP.x, windowWidth), wrap(maybeNextP.y, windowHeight))
  }

  override def shouldLive: Boolean = true

  override def bounds = new Rectangle(position.roundX - SIZE / 10, position.roundY - SIZE / 10, SIZE, SIZE)

  override def collidedWith(ve: VisibleEntity): Unit = {}

  override def toString: String = position.toString
}

object NPlayer {
  val SIZE = 20
}