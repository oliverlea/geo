package geo

import java.awt.event._
import java.awt.{Graphics, Point}
import javax.swing.{AbstractAction, Action, JPanel, KeyStroke}

import geo.domain.{VisibleEntity, Square, GPoint, Movement}

/**
 * @author Oliver Lea
 */
class GeoPanel extends JPanel {

	// constructor

	val im = getInputMap
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), KeyEvent.VK_W)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), KeyEvent.VK_A)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), KeyEvent.VK_S)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), KeyEvent.VK_D)

	val am = getActionMap

	// Members and methods

	var visibleEntites: List[VisibleEntity] = List(new Square(this, new GPoint(20, 20)))

	def tick() = {
		visibleEntites.foreach(ve => ve.tick())
	}

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		visibleEntites.foreach(ve => ve.render(g))
	}
}
