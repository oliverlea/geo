package geo.domain

import java.awt.Point

/**
 * @author Paulius Imbrasas
 * @author Oliver Lea
 */
class GPoint(val x: Double, val y: Double) {

	def this(p: Point) = this(p.getX, p.getY)

	def +(p: (Double, Double)) = {
		new GPoint(x + p._1, y + p._2)
	}

	def +(m: Momentum) = {
		new GPoint(x + m.dx, y + m.dy)
	}

	def -(p: (Double, Double)) = {
		new GPoint(x - p._1, y - p._2)
	}

	def -(m: Momentum) = {
		new GPoint(x - m.dx, y - m.dy)
	}
}
