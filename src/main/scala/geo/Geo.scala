package geo

import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.swing.{JFrame, SwingUtilities}

import geo.screens.{MainMenu, Screen}

import scala.compat.Platform

/**
  * @author Oliver Lea
  */
class Geo extends JFrame with Runnable {

  import geo.Implicits.convertLambdaToAction

  val MAX_TICKS_PER_SECOND = 60
  val OPTIMAL_TICK_TIME: Double = 1000 / MAX_TICKS_PER_SECOND

  private var currentScreen: Screen = new MainMenu(this)

  private var am = currentScreen.getActionMap
  private var running: Boolean = true

  add(currentScreen)
  am.put(KeyEvent.VK_ESCAPE, () => pause())

  def pause() = {
    running = !running
  }

  def screen_=(scr: Screen) = {
    add(scr)
    pack()
    remove(currentScreen)
    currentScreen = scr
    am = currentScreen.getActionMap

    am.put(KeyEvent.VK_ESCAPE, () => pause())
  }

  def screen = currentScreen

  override def run(): Unit = {
    def run(lastFpsTime: Long, lastFps: Long, lastTickStart: Long): Unit = {
      val now = Platform.currentTime
      val updateLength = now - lastTickStart
      val tickStart = now

      var fpsTime = lastFpsTime + updateLength
      var fps = lastFps + 1

      if (fpsTime > 1000) {
        setTitle(s"Geo $fps fps")
        fpsTime = 0
        fps = 0
      }

      if (running) {
        var delta: Double = updateLength / OPTIMAL_TICK_TIME
        while (delta >= 1) {
          SwingUtilities.invokeAndWait(new Runnable {
            override def run(): Unit = currentScreen.tick(delta)
          })
          delta -= OPTIMAL_TICK_TIME
        }
      }

      SwingUtilities.invokeAndWait(new Runnable {
        override def run(): Unit = currentScreen.repaint()
      })

      val sleepTime = tickStart - Platform.currentTime + OPTIMAL_TICK_TIME.toInt
      if (sleepTime > 0) {
        Thread sleep sleepTime
      }

      run(fpsTime, fps, tickStart)
    }
    val currentTime = Platform.currentTime
    run(Platform.currentTime, 0, currentTime)
  }
}

object Geo {
  val DEBUG = false

  def main(args: Array[String]): Unit = {
    SwingUtilities.invokeLater(new Runnable {
      override def run(): Unit = {
        val geo = new Geo
        geo.setMinimumSize(new Dimension(800, 600))
        geo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        geo.setVisible(true)
        new Thread(geo).start()
      }
    })
  }
}
