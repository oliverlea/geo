package geo

import java.awt.Dimension
import javax.swing.{JFrame, SwingUtilities}

import scala.compat.Platform

/**
 * @author Oliver Lea
 */
object Geo {

	def main(args: Array[String]): Unit = {
		SwingUtilities.invokeLater(new Runnable {
			override def run(): Unit = {
				val geoPanel = new GeoPanel
				val geo = new Geo(geoPanel)
				geo.setTitle("Geo")
				geo.setMinimumSize(new Dimension(800, 600))
				geo.add(geoPanel)
				geo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
				geo.setVisible(true)
				new Thread(geo).start()
			}
		})
	}
}

class Geo(geoPanel: GeoPanel) extends JFrame with Runnable {

	val MAX_TICKS_PER_SECOND = 60
	val OPTIMAL_TICK_TIME: Double = 1000 / MAX_TICKS_PER_SECOND

	override def run(): Unit = {
		def run(lastFpsTime: Long, lastFps: Long, lastTickStart: Long): Unit = {
			val now = Platform.currentTime
			val updateLength = now - lastTickStart
			val tickStart = now

			var fpsTime = lastFpsTime + updateLength
			var fps = lastFps + 1

			if (fpsTime > 1000) {
				println(s"FPS: $fps")
				fpsTime = 0
				fps = 0
			}

			var delta: Double = updateLength / OPTIMAL_TICK_TIME
			while (delta >= 1) {
				//tick()
				delta -= OPTIMAL_TICK_TIME
			}

			SwingUtilities.invokeAndWait(new Runnable {
				override def run(): Unit = geoPanel.repaint()
			})

			val sleepTime = tickStart - Platform.currentTime + OPTIMAL_TICK_TIME.toInt
			if (sleepTime > 0) {
				Thread sleep sleepTime
			}

			var running = true
			if (running) run(fpsTime, fps, tickStart)
		}
		val currentTime = Platform.currentTime
		run(Platform.currentTime, 0, currentTime)
	}
}
