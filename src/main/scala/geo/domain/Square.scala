package geo.domain

import java.awt.event.{ActionEvent, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Graphics2D, Point}
import javax.swing.{AbstractAction, Action, KeyStroke}

import geo.GeoPanel
import geo.domain.Square._

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class Square(private val gp: GeoPanel,
              var position: GPoint,
              var heading: Velocity) extends VisibleEntity {

	// Constructor

  private var fire = false

	private var keysHeld = Map(
		Direction.UP -> false,
		Direction.LEFT -> false,
		Direction.DOWN -> false,
		Direction.RIGHT -> false
	)
	gp.am.put(KeyEvent.VK_W, () => pressed(Direction.UP))
	gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), () => released(Direction.UP))
	gp.am.put(KeyEvent.VK_A, () => pressed(Direction.LEFT))
	gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), () => released(Direction.LEFT))
	gp.am.put(KeyEvent.VK_S, () => pressed(Direction.DOWN))
	gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), () => released(Direction.DOWN))
	gp.am.put(KeyEvent.VK_D, () => pressed(Direction.RIGHT))
	gp.am.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), () => released(Direction.RIGHT))
	gp.am.put(KeyEvent.VK_SPACE, () => fire = true)

	implicit def convertLambdaToAction(f: () => Unit): Action = new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			f()
		}
	}

	gp.addMouseListener(new MouseAdapter {
		override def mousePressed(e: MouseEvent): Unit = positionToPoint(e.getPoint)
	})

	gp.addMouseMotionListener(new MouseAdapter {
		override def mouseDragged(e: MouseEvent): Unit = positionToPoint(e.getPoint)
	})

	def positionToPoint(newPoint: Point): Unit = {
		position = new GPoint(newPoint)
	}

	private var velocity = new Velocity(0, 0)

	private var ticks: Double = 0

	override def tick(delta: Double): Unit = {
		keysHeld.filter(_._2).foreach(kh => velocity = accelerate(velocity, kh._1, delta))
		if (!velocity.stationary) {
			heading = velocity.heading
			ticks += delta
			if (ticks >= TICKS_TILL_SLOW_DOWN) {
				velocity = reduceAcceleration(delta * DECELERATION_PER_TICK)
				if (velocity.stationary)
					ticks = 0
			}
		}
    if (fire) {
      gp.addEntity(new Bullet(gp, position, heading))
      fire = false
    }
		position += velocity
	}

	override def render(g: Graphics2D): Unit = {
		g.drawRect(math.round(position.x - 10).toInt, math.round(position.y - 10).toInt, 20, 20)
	}

	def pressed(direction: Direction.Value): Unit = {
		keysHeld += (direction -> true)
	}

	def released(direction: Direction.Value): Unit = {
		keysHeld += (direction -> false)
	}

	private def accelerate(vel: Velocity, direction: Direction.Value, factor: Double): Velocity = {
		val toAddVel = INITIAL_VELOCITY * factor
		direction match {
			case Direction.UP => new Velocity(vel.dx, vel.dy - toAddVel)
			case Direction.DOWN => new Velocity(vel.dx, vel.dy + toAddVel)
			case Direction.LEFT => new Velocity(vel.dx - toAddVel, vel.dy)
			case Direction.RIGHT => new Velocity(vel.dx + toAddVel, vel.dy)
		}
	}

	private def reduceAcceleration(amount: Double): Velocity = {
		val newX: Double = velocity.dx match {
			case dx if dx > 0 => if ((dx - amount) > 0) dx - amount else 0
			case dx if dx < 0 => if ((dx + amount) < 0) dx + amount else 0
			case dx => dx
		}
		val newY: Double = velocity.dy match {
			case dy if dy > 0 => if ((dy - amount) > 0) dy - amount else 0
			case dy if dy < 0 => if ((dy + amount) < 0) dy + amount else 0
			case dy => dy
		}
		new Velocity(newX, newY)
	}
}

object Square {
	val MAX_SPEED = 5
	val INITIAL_VELOCITY = 0.5
	val ACCELERATION_PER_TICK = 1.005
	val DECELERATION_PER_TICK = 0.025
	val TICKS_TILL_SLOW_DOWN = 50
}
