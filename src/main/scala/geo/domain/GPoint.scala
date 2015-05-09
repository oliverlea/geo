package geo.domain

import java.awt.Point

/**
 * @author Paulius Imbrasas dev@crembo.eu
 * @author Oliver Lea
 */
class GPoint(_x: Int, _y: Int) extends Point(_x, _y) {

	def this(p: Point) = this(p.x, p.y)

	def +(p: (Int, Int)) = {
		new GPoint(x + p._1, y + p._2)
	}
	
	def -(p: (Int, Int)) = {
		new GPoint(x - p._1, y - p._1)
	}
}
