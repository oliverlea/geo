package geo.domain

import geo.domain.Movement.Movement

/**
 * @author Paulius Imbrasas
 */
class Cube(private val _position: GPoint) {

	val MAX_SPEED = 5
	val MOMENTUM_LENGTH = 50

	private val momentum = new Momentum(0, 0)

	private var ticks = 0

	def position = _position

	def position_=(position: GPoint) {
		_position.x = position.x
		_position.y = position.y
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
		if (momentum.x < MAX_SPEED) momentum.x += x
		if (momentum.y < MAX_SPEED) momentum.y += y
	}

	private def reduceMomentum() = {
		if (momentum.x > 0) momentum.x -= 1
		if (momentum.x < 0) momentum.x += 1

		if (momentum.y > 0) momentum.y -= 1
		if (momentum.y < 0) momentum.y += 1
	}

	def act(): Unit = {
		if (momentum.exists()) {
			ticks += 1
			if (ticks == MOMENTUM_LENGTH) {
				reduceMomentum()
				ticks = 0
			}
		}
		_position.x += momentum.x
		_position.y += momentum.y
	}
}

class Momentum(var x: Int, var y: Int) {
	def exists(): Boolean = x != 0 || y != 0
}

object Movement extends Enumeration {
	type Movement = Value
	val UP, DOWN, RIGHT, LEFT = Value
}