package geo.domain

import java.awt.Graphics2D
import java.awt.event.{ActionEvent, KeyEvent}
import javax.swing.{AbstractAction, Action, KeyStroke}

import geo.GeoPanel
import geo.domain.Square._

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class Square(private val gp: GeoPanel,
						 private val initialVelocity: Velocity,
						 private var position: GPoint) extends VisibleEntity(gp, initialVelocity, position) {

	// Constructor

	private var velocity = initialVelocity

	private var fire = false
  private var firingDirection = new Velocity(0, 0)
  private var fireCountdown: Double = 0

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
	gp.am.put(KeyEvent.VK_SPACE, () => fire = true)

  gp.addMouseHandler((coordinates, held) => {
    fire = held
    if (held) firingDirection = new Velocity(coordinates.x, coordinates.y)
  })

	private implicit def convertLambdaToAction(f: () => Unit): Action = new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			f()
		}
	}

	override def tick(delta: Double): Unit = {
		keysHeld.filter(_._2.held).foreach(kh => {
			velocity = velocity.linearAccelerate(kh._1, delta * ACCELERATION_PER_TICK)
			if (math.abs(velocity.dx) > MAX_SPEED)
				velocity = new Velocity(if (velocity.dx > 0) MAX_SPEED else -MAX_SPEED, velocity.dy)
			if (math.abs(velocity.dy) > MAX_SPEED)
				velocity = new Velocity(velocity.dx, if (velocity.dy > 0) MAX_SPEED else -MAX_SPEED)
			if ((velocity.dy * velocity.dy + velocity.dx + velocity.dx) > MAX_SPEED * MAX_SPEED) {
				velocity = velocity.normalize * MAX_SPEED
			}
		})

		if (!velocity.stationary) {
			keysHeld.foreach(hk => {
				val kh: KeyInfo = hk._2
        if (!kh.held && kh.ticksHeld >= Square.TICKS_TILL_SLOW_DOWN) {
					velocity = velocity.scaleAccelerate(hk._1, 1 - (delta * (1 - DECELERATION_FACTOR_PER_TICK)))
				}
				if (velocity.stationaryInDirection(hk._1))
					keysHeld += hk._1 -> new KeyInfo(kh.held, 0)
				else
					keysHeld += hk._1 -> new KeyInfo(kh.held, kh.ticksHeld + 1)
			})
		}

    fireCountdown -= delta
    if (fire && fireCountdown <= 0) {
      gp.addEntity(new Bullet(gp, firingDirection.normalize * Bullet.SPEED, position))
      fireCountdown = FIRE_DELAY
		}

    position += velocity
	}

	override def render(g: Graphics2D): Unit = {
		g.drawRect(math.round(position.x - 10).toInt, math.round(position.y - 10).toInt, 20, 20)
	}

	def pressedDirection(direction: Direction.Value): Unit = {
		keysHeld += (direction -> new KeyInfo(true, 0))
	}

	def releasedDirection(direction: Direction.Value): Unit = {
		keysHeld += (direction -> new KeyInfo(false, 0))
	}
}

object Square {
	val MAX_SPEED = 5
	val ACCELERATION_PER_TICK = 0.12 // Linear
	val DECELERATION_FACTOR_PER_TICK = 0.98 // Non-linear
	val TICKS_TILL_SLOW_DOWN = 10
  val FIRE_DELAY = 5 // ticks
}
