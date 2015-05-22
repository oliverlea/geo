package geo.network

import java.io.{IOException, ObjectInputStream, ObjectOutputStream}

import geo.network.Networking.{Empty, Failed, Reading, Successful}

/**
 * @author Paulius Imbrasas
 */
abstract class Networker {

  private def read(in: ObjectInputStream): Reading =
    try in.readObject() match {
      case data: Packet => Successful(data)
      case _ => Empty()
    } catch {
      case e: IOException =>
        e.printStackTrace()
        Failed(e)
    }

  def send(ostream: ObjectOutputStream, packet: Packet): Unit = {
    ostream.writeObject(packet)
  }

  /**
   * Handles receiving a packet
   *
   * @param istream input stream
   * @return maybe a packet, if reading is successful
   *
   * @throws IOException if reading failed
   */
  def receive(istream: ObjectInputStream): Option[Packet] = read(istream) match {
    case Successful(data) => Option(data)
    case Failed(e) => throw e
    case Empty() => None
  }
}

package object Networking {

  trait Reading
  case class Successful(data: Packet) extends Reading
  case class Failed(e: Exception) extends Reading
  case class Empty() extends Reading
}