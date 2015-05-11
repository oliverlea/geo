package geo.domain

import java.awt.event.{ActionEvent, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Graphics, Point}
import javax.swing.{KeyStroke, AbstractAction, Action}

import geo.GeoPanel
import geo.domain.Square.{ACCELERATION_FACTOR_PER_TICK, MAX_SPEED, TICKS_TILL_SLOW_DOWN}

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class Square(private val gp: GeoPanel, var position: GPoint) extends VisibleEntity {

	// Constructor

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
		keysHeld.filter(_._2).foreach(kh => accelerate(kh._1, delta))
		if (!velocity.stationary) {
			ticks += delta
			if (ticks >= TICKS_TILL_SLOW_DOWN) {
				reduceAcceleration(delta)
				if (velocity.stationary)
					ticks = 0
			}
		}
		position += velocity * delta
	}

	override def render(g: Graphics): Unit = {
		g.drawRect(math.round(position.x - 10).toInt, math.round(position.y - 10).toInt, 20, 20)
	}

	def pressed(direction: Direction.Value): Unit = {
		keysHeld += (direction -> true)
	}

	def released(direction: Direction.Value): Unit = {
		keysHeld += (direction -> false)
	}

	private def accelerate(direction: Direction.Value, factor: Double): Unit = {
		velocity += (direction match {
			case Direction.UP => move(velocity, 0, -ACCELERATION_FACTOR_PER_TICK) * factor
			case Direction.DOWN => move(velocity, 0, ACCELERATION_FACTOR_PER_TICK) * factor
			case Direction.LEFT => move(velocity, -ACCELERATION_FACTOR_PER_TICK, 0) * factor
			case Direction.RIGHT => move(velocity, ACCELERATION_FACTOR_PER_TICK, 0) * factor
		})
	}

	private def move(acc: Velocity, x: Double, y: Double): Velocity = {
		new Velocity(
			if (math.abs(velocity.dx) < MAX_SPEED) x else 0,
			if (math.abs(velocity.dy) < MAX_SPEED) y else 0
		)
	}

	private def reduceAcceleration(delta: Double) = {
		val change: Double = delta * Square.DECCELERATION_FACTOR_PER_TICK
		val newX: Double = velocity.dx match {
			case dx if dx > 0 => if ((dx - change) > 0) dx - change else 0
			case dx if dx < 0 => if ((dx + change) < 0) dx + change else 0
			case dx => dx
		}
		val newY: Double = velocity.dy match {
			case dy if dy > 0 => if ((dy - change) > 0) dy - change else 0
			case dy if dy < 0 => if ((dy + change) < 0) dy + change else 0
			case dy => dy
		}
		velocity = new Velocity(newX, newY)
	}
}

object Square {
	val MAX_SPEED = 5
	val ACCELERATION_FACTOR_PER_TICK = 0.1
	val DECELERATION_FACTOR_PER_TICK = 0.035
	val TICKS_TILL_SLOW_DOWN = 50
}