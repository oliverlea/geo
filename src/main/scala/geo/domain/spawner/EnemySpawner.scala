package geo.domain.spawner

import geo.domain.spawner.EnemySpawner.TICKS_PER_GENERATE
import geo.domain.{Enemy, GPoint, Velocity}
import geo.screens.GeoPanel

import scala.util.Random

/**
 * @author Oliver Lea
 */
class EnemySpawner(private val gp: GeoPanel) extends VisibleEntitySpawner[Enemy](gp) {

  private var tickCount: Double = 0

  override def spawnVisibleEntities(delta: Double, r: Random): List[Enemy] = {
    if (tickCount >= TICKS_PER_GENERATE) {
      tickCount = 0
      val randomEdgeP = randomEdgePoint(r)
      List(new Enemy(gp, randomDirectionFromPoint(r, randomEdgeP).normalize * Enemy.SPEED, randomEdgeP))
    } else {
      tickCount += delta
      List()
    }
  }

  private def randomEdgePoint(r: Random): GPoint = {
    val xAxis = r.nextBoolean()
    val inverse = r.nextBoolean()
    if (xAxis)
      new GPoint(r.nextDouble() * gp.getWidth, if (inverse) gp.getHeight else 0)
    else
      new GPoint(if (inverse) gp.getWidth else 0, r.nextDouble() * gp.getHeight)
  }

  private def randomDirectionFromPoint(r: Random, p: GPoint): Velocity = {
    val x = r.nextDouble()
    val dx = if (p.x == gp.getWidth) -x else x
    val y = r.nextDouble()
    val dy = if (p.y == gp.getHeight) -y else y
    new Velocity(dx, dy)
  }
}

object EnemySpawner {
  val TICKS_PER_GENERATE = 1
}
