package geo.domain

import java.awt.Point

/**
 * @author Paulius Imbrasas dev@crembo.eu
 */
class GPoint(_x: Int, _y: Int) extends Point(_x, _y) {

	def this(p: Point) = this(p.x, p.y)

	def +(point: Point) = {
		new GPoint(x + point.x, y + point.y)
	}
}
