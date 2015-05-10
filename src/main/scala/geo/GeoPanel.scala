package geo

import java.awt.event._
import java.awt.{Graphics, Point}
import javax.swing.{AbstractAction, Action, JPanel, KeyStroke}

import geo.domain.{VisibleEntity, Square, GPoint, Direction}

/**
 * @author Oliver Lea
 */
class GeoPanel extends JPanel {

	// constructor

	val im = getInputMap
	private val W_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true)
	private val A_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true)
	private val S_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true)
	private val D_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), KeyEvent.VK_W)
	im.put(W_RELEASED, W_RELEASED)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), KeyEvent.VK_A)
	im.put(A_RELEASED, A_RELEASED)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), KeyEvent.VK_S)
	im.put(S_RELEASED, S_RELEASED)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), KeyEvent.VK_D)
	im.put(D_RELEASED, D_RELEASED)

	val am = getActionMap

	// Members and methods

	var visibleEntites: List[VisibleEntity] = List(new Square(this, new GPoint(20, 20)))

	def tick(delta: Double) = {
		visibleEntites.foreach(ve => ve.tick(delta))
	}

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		visibleEntites.foreach(ve => ve.render(g))
	}
}
