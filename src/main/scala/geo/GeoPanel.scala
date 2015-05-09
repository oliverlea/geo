package geo

import java.awt.Graphics
import java.awt.event._
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

	addAction(KeyEvent.VK_W, () => position +=(0, -5))
	addAction(KeyEvent.VK_A, () => position +=(-5, 0))
	addAction(KeyEvent.VK_S, () => position +=(0, 5))
	addAction(KeyEvent.VK_D, () => position +=(5, 0))

	def addAction(key: Int, action: () => Unit): Unit = {
		am.put(key, new AbstractAction() {
			override def actionPerformed(e: ActionEvent): Unit = {
				action()
				repaint()
			}
		})
	}

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		println(position)
		g.drawRect(position.x, position.y, 20, 20)
	}
}
