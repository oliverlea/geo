package geo.network

import java.io.{IOException, ObjectInputStream, ObjectOutputStream}

import geo.network.Networking.{Empty, Failed, Reading, Successful}

/**
 * @author Paulius Imbrasas
 */
abstract class Networker {

  private def read(in: ObjectInputStream): Reading = try {
    in.readObject() match {
      case data: Packet =>
        println(s"Successful reading $data")
        Successful(data)
      case _ =>
        println("Empty packet")
        Empty()
    }
  } catch {
    case e: IOException =>
      e.printStackTrace()
      Failed(e)
  }

  def send(out: ObjectOutputStream, packet: Packet): Unit = {
    println(s"Sending: $packet")
    out.writeObject(packet)
  }

  def receive(in: ObjectInputStream): Option[Packet] = read(in) match {
    case Successful(data) =>
      println(s"Received: $data")
      Option(data)
    case Failed(e) =>
      println(s"Failed: $e")
      e.printStackTrace()
      throw e
    case Empty() =>
      println(s"Empty")
      None
  }
}

package object Networking {
  type PacketIn = Option[Packet] => Any
  type PacketOut = () => Packet

  trait Reading
  case class Successful(data: Packet) extends Reading
  case class Failed(e: Throwable) extends Reading
  case class Empty() extends Reading
}