package geo

import java.awt.event._
import java.awt.{Graphics, Point}
import javax.swing.{AbstractAction, Action, JPanel, KeyStroke}

import geo.domain.{Cube, GPoint, Movement}

/**
 * @author Oliver Lea
 */
class GeoPanel extends JPanel {

	val cube = new Cube(new GPoint(20, 20))

	override def paintComponent(g: Graphics): Unit = {
		super.paintComponent(g)
		cube.act()
		g.drawRect(cube.position.x - 10, cube.position.y - 10, 20, 20)
	}

	// constructor

	addMouseListener(new MouseAdapter {
		override def mousePressed(e: MouseEvent): Unit = positionToPoint(e.getPoint)
	})

	addMouseMotionListener(new MouseAdapter {
		override def mouseDragged(e: MouseEvent): Unit = positionToPoint(e.getPoint)
	})

	def positionToPoint(newPoint: Point): Unit = {
		cube.position = new GPoint(newPoint)
	}

	val im = getInputMap
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), KeyEvent.VK_W)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), KeyEvent.VK_A)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), KeyEvent.VK_S)
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), KeyEvent.VK_D)

	val am = getActionMap
	am.put(KeyEvent.VK_W, () => cube.move(Movement.UP))
	am.put(KeyEvent.VK_A, () => cube.move(Movement.LEFT))
	am.put(KeyEvent.VK_S, () => cube.move(Movement.DOWN))
	am.put(KeyEvent.VK_D, () => cube.move(Movement.RIGHT))

	implicit def convertLambdaToAction(f: () => Unit): Action = new AbstractAction() {
		override def actionPerformed(e: ActionEvent): Unit = {
			f()
			repaint()
		}
	}
}
