package geo

import java.awt.{Point, Graphics}
import java.awt.event._
import javax.swing.{Action, AbstractAction, JPanel, KeyStroke}

import geo.domain.GPoint

/**
 * @author Oliver Lea
 */
class GeoPanel extends JPanel {

	@volatile var position = new GPoint(20, 20)

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		g.drawRect(position.x - 10, position.y - 10, 20, 20)
	}

	// constructor

	addMouseListener(new MouseAdapter {
		override def mousePressed(e: MouseEvent): Unit = positionToPoint(e.getPoint)
	})
	addMouseMotionListener(new MouseAdapter {
		override def mouseDragged(e: MouseEvent): Unit = positionToPoint(e.getPoint)
	})

	def positionToPoint(newPoint: Point): Unit = {
		position = new GPoint(newPoint)
	}

	val im = getInputMap
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), KeyEvent.VK_W)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), KeyEvent.VK_A)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), KeyEvent.VK_S)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), KeyEvent.VK_D)

	val am = getActionMap
	am.put(KeyEvent.VK_W, () => position -= (0, 5))
	am.put(KeyEvent.VK_A, () => position -= (5, 0))
	am.put(KeyEvent.VK_S, () => position += (0, 5))
	am.put(KeyEvent.VK_D, () => position += (5, 0))

	implicit def convertLambdaToAction(f: () => Unit): Action = new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = f()
	}
}
