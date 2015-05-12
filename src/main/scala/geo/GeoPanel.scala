package geo

import java.awt.{Graphics2D, Graphics}
import java.awt.event._
import javax.swing.{JPanel, KeyStroke}

import geo.domain._

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

  im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), KeyEvent.VK_SPACE)

  val am = getActionMap

  // Members and methods

  var visibleEntities: List[VisibleEntity] = List(
    new Square(this, new GPoint(20, 20), new Velocity(0, 0))
  )

  def tick(delta: Double) = {
    visibleEntities.foreach(_.tick(delta))
    visibleEntities = visibleEntities.filterNot(_.visible)
  }

  def addEntity(entity: VisibleEntity): Unit = {
    visibleEntities = entity :: visibleEntities
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    visibleEntities.foreach(_.render(g.asInstanceOf[Graphics2D]))
  }
}
