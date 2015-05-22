package geo

import java.awt.event._
import java.awt.{Graphics, Graphics2D}
import javax.swing.{AbstractAction, JPanel, KeyStroke, SwingUtilities}

import geo.domain._
import geo.domain.spawner.{EnemySpawner, VisibleEntitySpawner}
import geo.network._
import geo.structure.QuadTree

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

  im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), KeyEvent.VK_C)

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
  private val enemyRandom = new Random(Platform.currentTime)

  private var mouseHandlers: List[MouseHandler] = List()

  private val player = new Player(this, new Velocity(0, 0), new GPoint(20, 20))
  private val nplayer = new NPlayer(this, new Velocity(0, 0), new GPoint(0, 0))

  private val network = new Multiplayer(player, nplayer)
  am.put(KeyEvent.VK_C, new AbstractAction() {
    override def actionPerformed(e: ActionEvent): Unit = network.connect()
  })

  private var visibleEntities: List[VisibleEntity] = List(
    player, nplayer
  )
  private val visibleEntitySpawners: List[VisibleEntitySpawner[_ <: VisibleEntity]] = List(
    new EnemySpawner(this)
  )

  def tick(delta: Double) = {
    visibleEntities.foreach(_.tick(delta))
    visibleEntities = visibleEntities.filter(_.shouldLive)
    visibleEntities = visibleEntities ::: generateVisibleEntities(visibleEntitySpawners, delta)
    network.tick(delta)
    detectCollisions(visibleEntities)
  }

  private def detectCollisions(ves: Seq[VisibleEntity]): Unit = {
    val qt = new QuadTree[VisibleEntity](0, 0, getWidth, getHeight)
    for (ve <- ves) {
      qt.set(ve.position, ve)
    }

    for (nearElements <- qt.getElements) {
      for (e <- nearElements) {
        for (e2 <- nearElements) {
          if (e != e2 && e.bounds.intersects(e2.bounds)) {
            e.collidedWith(e2)
          }
        }
      }
    }
  }

  def generateVisibleEntities(ves: Seq[VisibleEntitySpawner[_ <: VisibleEntity]],
                              delta: Double): List[VisibleEntity] = {
    enemyRandom.setSeed(Platform.currentTime)
    ves.flatMap(_.spawnVisibleEntities(delta, enemyRandom)).toList
  }

  def addEntity(entity: VisibleEntity): Unit = {
    visibleEntities = entity :: visibleEntities
  }

  def addMouseHandler(handler: MouseHandler): Unit = {
    mouseHandlers = handler :: mouseHandlers
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2d = g.asInstanceOf[Graphics2D]
    visibleEntities.foreach(_.render(g2d))
    drawQuadTree(g, visibleEntities)
  }

  private def drawQuadTree(g: Graphics, ves: Seq[VisibleEntity]): Unit = {
    val qt = new QuadTree[VisibleEntity](0, 0, getWidth, getHeight)
    for (ve <- ves) {
      qt.set(ve.position, ve)
    }
    for (leaf <- qt.getLeaves) {
      g.drawLine(leaf.x.toInt, leaf.y.toInt, (leaf.x + leaf.width).toInt, leaf.y.toInt)
      g.drawLine(leaf.x.toInt, leaf.y.toInt, leaf.x.toInt, (leaf.y + leaf.height).toInt)
      g.drawLine(leaf.x.toInt, (leaf.y + leaf.height).toInt, (leaf.x + leaf.width).toInt, (leaf.y + leaf.height).toInt)
      g.drawLine((leaf.x + leaf.width).toInt, (leaf.y + leaf.height).toInt, (leaf.x + leaf.width).toInt, leaf.y.toInt)
    }
  }
}
