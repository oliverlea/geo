package geo.network

import java.io.{IOException, ObjectInputStream, ObjectOutputStream}
import java.net.Socket

/**
 * @author Paulius Imbrasas
 */
abstract class Networker[A] {

  private def read(in: ObjectInputStream): Option[A] = try {
    in.readObject() match {
      case player: A => Option(player)
      case _ => None
    }
  } catch {
    case _: IOException => None
  }

  protected def receive(socket: Socket): Option[A] = {
    println("Receiving")
    val in = new ObjectInputStream(socket.getInputStream)
    val resp = read(in)

    in.close()
    socket.close()

    resp
  }

  protected def send(packet: A)(socket: Socket): Unit = {
    println(s"Sending $packet")
    val out = new ObjectOutputStream(socket.getOutputStream)
    out.writeObject(packet)
    out.close()
  }
}