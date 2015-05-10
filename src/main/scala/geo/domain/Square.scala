package geo.domain

import java.awt.{Point, Graphics}
import java.awt.event.{MouseEvent, MouseAdapter, ActionEvent, KeyEvent}
import javax.swing.{AbstractAction, Action, ActionMap}

import geo.GeoPanel
import geo.domain.Movement.Movement
import geo.domain.Square.{MOMENTUM_LENGTH, MAX_SPEED}

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class Square(private val gp: GeoPanel, var position: GPoint) extends VisibleEntity {

	gp.am.put(KeyEvent.VK_W, () => move(Movement.UP))
	gp.am.put(KeyEvent.VK_A, () => move(Movement.LEFT))
	gp.am.put(KeyEvent.VK_S, () => move(Movement.DOWN))
	gp.am.put(KeyEvent.VK_D, () => move(Movement.RIGHT))

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

	private val momentum = new Momentum(0, 0)

	private var ticks = 0

	override def tick(): Unit = {
		if (momentum.exists) {
			ticks += 1
			if (ticks == MOMENTUM_LENGTH) {
				reduceMomentum()
				ticks = 0
			}
		}
		position += momentum
	}

	override def render(g: Graphics): Unit = {
		g.drawRect(position.x.toInt - 10, position.y.toInt - 10, 20, 20)
	}

	def move(movement: Movement): Unit = {
		movement match {
			case Movement.UP => move(0, -1)
			case Movement.DOWN => move(0, 1)
			case Movement.LEFT => move(-1, 0)
			case Movement.RIGHT => move(1, 0)
		}
	}

	private def move(x: Int, y: Int): Unit = {
		if (momentum.dx < MAX_SPEED)
			momentum.dx += x
		if (momentum.dy < MAX_SPEED)
			momentum.dy += y
	}

	private def reduceMomentum() = {
		if (momentum.dx > 0)
			momentum.dx -= 1
		else if (momentum.dx < 0)
			momentum.dx += 1

		if (momentum.dy > 0)
			momentum.dy -= 1
		else if (momentum.dy < 0)
			momentum.dy += 1
	}
}

object Square {
	val MAX_SPEED = 5
	val MOMENTUM_LENGTH = 50
}

class Momentum(var dx: Double, var dy: Double) {
	def exists: Boolean = (dx != 0) || (dy != 0)
}

object Movement extends Enumeration {
	type Movement = Value
	val UP, DOWN, RIGHT, LEFT = Value
}