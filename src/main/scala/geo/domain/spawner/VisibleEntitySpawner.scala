package geo.domain.spawner

import geo.GeoPanel
import geo.domain.VisibleEntity

import scala.util.Random

/**
 * @author Oliver Lea
 */
abstract class VisibleEntitySpawner[T <: VisibleEntity](private val gp: GeoPanel) {
  def spawnVisibleEntity(delta: Double, r: Random): Option[T]
}
