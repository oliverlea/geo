package geo.domain.spawner

import geo.domain.VisibleEntity
import geo.screens.GeoPanel

import scala.util.Random

/**
 * @author Oliver Lea
 */
abstract class VisibleEntitySpawner[T <: VisibleEntity](private val gp: GeoPanel) {
  def spawnVisibleEntities(delta: Double, r: Random): List[T]
}
