package geo.network

import geo.domain.{GPoint, Velocity}

/**
 * @author Paulius Imbrasas
 */
class Packet(val position: GPoint,
             val velocity: Velocity,
             val sender: String) extends Serializable {

  def this() = {
    this(null, null, null)
  }

  override def toString: String = s"Position: $position; Velocity: $velocity; Sender: $sender"
}
