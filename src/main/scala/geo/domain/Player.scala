package geo.domain

import java.awt.event.KeyEvent
import java.awt.{Graphics2D, Rectangle}
import javax.swing.KeyStroke

import geo.Implicits.convertLambdaToAction
import geo.domain.Player._
import geo.screens.GeoPanel

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class Player(private val gp: GeoPanel,
             private val initialVelocity: Velocity,
             var position: GPoint) extends VisibleEntity(gp, initialVelocity) {

  // Constructor

  var _velocity = initialVelocity

  private var fire = false
  private var firingTarget = new GPoint(0, 0)
  private var fireCountdown: Double = 0
  private var networkCountdown: Double = 0

  private var keysHeld = Map(
    Direction.UP -> new KeyInfo(false, 0),
    Direction.LEFT -> new KeyInfo(false, 0),
    Direction.DOWN -> new KeyInfo(false, 0),
    Direction.RIGHT -> new KeyInfo(false, 0)
  )
  gp.am.put(KeyEvent.VK_W, () => pressedDirection(Direction.UP))
  gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), () => releasedDirection(Direction.UP))
  gp.am.put(KeyEvent.VK_A, () => pressedDirection(Direction.LEFT))
  gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), () => releasedDirection(Direction.LEFT))
  gp.am.put(KeyEvent.VK_S, () => pressedDirection(Direction.DOWN))
  gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), () => releasedDirection(Direction.DOWN))
  gp.am.put(KeyEvent.VK_D, () => pressedDirection(Direction.RIGHT))
  gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), () => releasedDirection(Direction.RIGHT))

  gp.addMouseHandler((coordinates, held) => {
    fire = held
    if (held)
      firingTarget = new GPoint(coordinates.x, coordinates.y)
  })

  override def tick(delta: Double): Unit = {
    keysHeld.filter(_._2.held).foreach(kh => {
      _velocity = _velocity.linearAccelerate(kh._1, delta * ACCELERATION_PER_TICK)
      _velocity = limitToMaxSpeed(_velocity, MAX_SPEED)
    })

    if (!_velocity.stationary) {
      keysHeld.foreach(hk => {
        val kh: KeyInfo = hk._2
        if (!kh.held && kh.ticksHeld >= Player.TICKS_TILL_SLOW_DOWN) {
          _velocity = _velocity.scaleAccelerate(hk._1, 1 - (delta * (1 - DECELERATION_FACTOR_PER_TICK)))
        }
        if (_velocity.stationaryInDirection(hk._1))
          keysHeld += hk._1 -> new KeyInfo(kh.held, 0)
        else
          keysHeld += hk._1 -> new KeyInfo(kh.held, kh.ticksHeld + 1)
      })
    }

    updateFire(delta)

    position = nextPosition(position, _velocity)
  }

  private def updateFire(delta: Double): Unit = {
    fireCountdown -= delta
    if (fire && fireCountdown <= 0) {
      val dxdy: GPoint = firingTarget - position
      gp.addEntity(new Bullet(gp, new Velocity(dxdy.x, dxdy.y).normalize * Bullet.SPEED, position))
      fireCountdown = FIRE_DELAY
    }
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

  def limitToMaxSpeed(vel: Velocity, maxSpeed: Double): Velocity = {
    var newVel: Velocity = vel
    if (math.abs(newVel.dx) > maxSpeed)
      newVel = new Velocity(if (newVel.dx > 0) maxSpeed else -maxSpeed, newVel.dy)
    if (math.abs(_velocity.dy) > maxSpeed)
      newVel = new Velocity(_velocity.dx, if (_velocity.dy > 0) maxSpeed else -maxSpeed)
    if ((newVel.dy * newVel.dy + newVel.dx + newVel.dx) > maxSpeed * maxSpeed) {
      newVel = newVel.normalize * maxSpeed
    }
    newVel
  }

  override def shouldLive: Boolean = true

  override def render(g: Graphics2D): Unit = {
    val b = bounds
    g.drawRect(b.x, b.y, b.width, b.height)
  }

  override def bounds = new Rectangle(math.round(position.x - 10).toInt, math.round(position.y - 10).toInt, 20, 20)

  override def collidedWith(ve: VisibleEntity): Unit = {
  }

  def pressedDirection(direction: Direction.Value): Unit = {
    keysHeld += (direction -> new KeyInfo(true, 0))
  }

  def releasedDirection(direction: Direction.Value): Unit = {
    keysHeld += (direction -> new KeyInfo(false, 0))
  }
}

object Player {
  val SIZE = 20
  val MAX_SPEED = 5
  val ACCELERATION_PER_TICK = 0.12
  // Linear
  val DECELERATION_FACTOR_PER_TICK = 0.98
  // Non-linear
  val TICKS_TILL_SLOW_DOWN = 10
  val FIRE_DELAY = 5
  // ticks
  val NETWORK_DELAY = 20
}
