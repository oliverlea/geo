package geo

import java.awt.event._
import java.awt.{Graphics, Graphics2D}
import javax.swing.{JPanel, KeyStroke, SwingUtilities}

import geo.domain._
import geo.domain.spawner.EnemySpawner

import scala.compat.Platform
import scala.util.Random

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

  type MouseHandler = (GPoint, Boolean) => Unit

  addMouseMotionListener(new MouseMotionAdapter {
    override def mouseDragged(e: MouseEvent): Unit = {
      if (SwingUtilities.isLeftMouseButton(e)) {
        val mousePosition = new GPoint(e.getX, e.getY)
        mouseHandlers.foreach(_(mousePosition, true))
      }
    }
  })

  addMouseListener(new MouseAdapter {

    override def mousePressed(e: MouseEvent): Unit = {
      if (SwingUtilities.isLeftMouseButton(e)) {
        val mousePosition = new GPoint(e.getX, e.getY)
        mouseHandlers.foreach(_(mousePosition, true))
      }
    }

    override def mouseReleased(e: MouseEvent): Unit = {
      if (SwingUtilities.isLeftMouseButton(e)) {
        val mousePosition = new GPoint(e.getX, e.getY)
        mouseHandlers.foreach(_(mousePosition, false))
      }
    }
  })

  // Members and methods
  private var mouseHandlers: List[MouseHandler] = List()

  private var visibleEntities: List[VisibleEntity] = List(
    new Player(this, new Velocity(0, 0), new GPoint(20, 20))
  )
  private val enemySpawner = new EnemySpawner(this)

  def tick(delta: Double) = {
    visibleEntities.foreach(_.tick(delta))
    visibleEntities = visibleEntities.filter(_.shouldLive)
    visibleEntities = generateVisibleEntities(delta) ::: visibleEntities
  }

  def generateVisibleEntities(delta: Double): List[VisibleEntity] = {
    var visibleEntities = List[VisibleEntity]()
    val r: Random = new Random(Platform.currentTime)
    val enemy = enemySpawner.spawnVisibleEntity(delta, r)
    if (enemy.isDefined)
      visibleEntities = enemy.get :: visibleEntities
    visibleEntities
  }

  def addEntity(entity: VisibleEntity): Unit = {
    visibleEntities = entity :: visibleEntities
  }

  def addMouseHandler(handler: MouseHandler): Unit = {
    mouseHandlers = handler :: mouseHandlers
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    visibleEntities.foreach(_.render(g.asInstanceOf[Graphics2D]))
  }
}
