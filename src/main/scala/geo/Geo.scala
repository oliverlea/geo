package geo

import java.awt.Dimension
import javax.swing.{JFrame, SwingUtilities}

/**
 * @author Oliver Lea
 */
object Geo {

	def main(args: Array[String]): Unit = {
		SwingUtilities.invokeLater(new Runnable {
			override def run(): Unit = {
				val g = new Geo
				g.setTitle("Geo")
				g.setMinimumSize(new Dimension(500, 500))
				g.add(new GeoPanel)
				g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
				g.setVisible(true)
			}
		})
	}
}

class Geo extends JFrame {

	def run(): Unit = {

	}
}
