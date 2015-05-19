package geo.network

import geo.domain.{GPoint, Velocity}

/**
 * @author Paulius Imbrasas
 */
class Packet(val position: GPoint,
             val velocity: Velocity,
             val sender: String) extends Serializable {
  override def toString: String = s"Position: $position; Velocity: $velocity; Sender: $sender"
}
