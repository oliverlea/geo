package geo

import java.awt.event._
import java.awt.{Graphics, Point}
import javax.swing.{AbstractAction, JPanel, KeyStroke}

import geo.domain.GPoint

/**
 * @author Oliver Lea
 */
class GeoPanel extends JPanel {

	@volatile var position = new GPoint(20, 20)

	setFocusable(true)
	requestFocusInWindow()

	addMouseMotionListener(new MouseAdapter {
		override def mouseDragged(e: MouseEvent): Unit = {
			position = new GPoint(e.getPoint)
			repaint()
		}
	})

	val im = getInputMap
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), KeyEvent.VK_W)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), KeyEvent.VK_A)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), KeyEvent.VK_S)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), KeyEvent.VK_D)

	val am = getActionMap

	am.put(KeyEvent.VK_W, new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			position += new Point(0, -5)
			repaint()
		}
	})
	am.put(KeyEvent.VK_A, new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			position += new Point(-5, 0)
			repaint()
		}
	})
	am.put(KeyEvent.VK_S, new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			position += new Point(0, 5)
			repaint()
		}
	})
	am.put(KeyEvent.VK_D, new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			position += new Point(5, 0)
			repaint()
		}
	})

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		println(position)
		g.drawRect(position.x, position.y, 20, 20)
	}
}
