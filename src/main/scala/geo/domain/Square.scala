package geo.domain

import java.awt.event.{ActionEvent, KeyEvent, MouseAdapter, MouseEvent}
import java.awt.{Graphics, Point}
import javax.swing.{AbstractAction, Action}

import geo.GeoPanel
import geo.domain.Direction.Movement
import geo.domain.Square.{MAX_SPEED, TICKS_TILL_SLOW_DOWN}

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class Square(private val gp: GeoPanel, var position: GPoint) extends VisibleEntity {

	// Constructor

	gp.am.put(KeyEvent.VK_W, () => move(Direction.UP))
	gp.am.put(KeyEvent.VK_A, () => move(Direction.LEFT))
	gp.am.put(KeyEvent.VK_S, () => move(Direction.DOWN))
	gp.am.put(KeyEvent.VK_D, () => move(Direction.RIGHT))

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

	private var acceleration = new Acceleration(0, 0)

	private var ticks: Double = 0

	override def tick(delta: Double): Unit = {
		if (!acceleration.stationary) {
			ticks += delta
			if (ticks >= TICKS_TILL_SLOW_DOWN) {
				reduceAcceleration(delta)
				if (acceleration.stationary)
					ticks = 0
			}
		}
		position += acceleration * delta
	}

	override def render(g: Graphics): Unit = {
		g.drawRect(math.round(position.x - 10).toInt, math.round(position.y.toInt - 10).toInt, 20, 20)
	}

	def move(movement: Movement): Unit = {
		movement match {
			case Direction.UP => move(0, -1)
			case Direction.DOWN => move(0, 1)
			case Direction.LEFT => move(-1, 0)
			case Direction.RIGHT => move(1, 0)
		}
	}

	private def move(x: Double, y: Int): Unit = {
		val nX = if (x < MAX_SPEED) x else 0
		val nY = if (y < MAX_SPEED) y else 0
		acceleration += new Acceleration(nX, nY)
	}

	private def reduceAcceleration(delta: Double) = {
		val change: Double = delta * Square.DECELERATION_FACTOR
		val newX: Double = acceleration.dx match {
			case dx if dx > 0 => if ((dx - change) > 0) dx - change else 0
			case dx if dx < 0 => if ((dx + change) < 0) dx + change else 0
			case dx => dx
		}
		val newY: Double = acceleration.dy match {
			case dy if dy > 0 => if ((dy - change) > 0) dy - change else 0
			case dy if dy < 0 => if ((dy + change) < 0) dy + change else 0
			case dy => dy
		}
		acceleration = new Acceleration(newX, newY)
	}
}

object Square {
	val MAX_SPEED = 5
	val DECELERATION_FACTOR = 0.035
	val TICKS_TILL_SLOW_DOWN = 50
}