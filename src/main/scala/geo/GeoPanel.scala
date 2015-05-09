package geo

import java.awt.event._
import java.awt.{Graphics, Point}
import javax.swing.JPanel

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

	addKeyListener(new KeyListener {

		override def keyTyped(e: KeyEvent): Unit = {
			val key = e.getKeyCode

			key match {
				case KeyEvent.VK_W =>
					position += new Point(0, 5)
				case KeyEvent.VK_A =>
					position += new Point(-5, 0)
				case KeyEvent.VK_S =>
					position += new Point(0, -5)
				case KeyEvent.VK_D =>
					position += new Point(5, 0)
				case _ =>
			}

			repaint()
		}

		override def keyPressed(e: KeyEvent): Unit = {

		}

		override def keyReleased(e: KeyEvent): Unit = {

		}
	})

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		println(position)
		g.drawRect(position.x, position.y, 20, 20)
	}
}
