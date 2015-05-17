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
        println("Failed reading")
        Empty()
    }
  } catch {
    case e: IOException =>
      println(s"Failed reading: ${e.getMessage}")
      Failed(e)
  }

  def send(out: ObjectOutputStream, packet: Packet): Unit = {
    println(s"Sending: $packet")
    out.writeObject(packet)
  }

  def receive(in: ObjectInputStream): Option[Packet] = {
    val resp = read(in)

    println(s"Receiving: $resp")

    //    val response = resp match {
    //      case Successful(data) if data.sender != Multiplayer.Identifier =>
    //        println(s"Received packet: $data")
    //        Option(data.position)
    //      case Successful(data) =>
    //        println(s"Received from self: $data")
    //        None
    //      case Failed(e) =>
    //        println(s"Error in packet: ${e.getMessage}")
    //        None
    //      case Empty() =>
    //        println("Empty packet")
    //        None
    //    }

    resp match {
      case Successful(data) => Option(data)
      case Failed(e) => throw e
      case Empty() => None
    }
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