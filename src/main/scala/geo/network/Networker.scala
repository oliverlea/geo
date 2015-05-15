package geo.network

import java.io.{IOException, ObjectInputStream, ObjectOutputStream}
import java.net.Socket

/**
 * @author Paulius Imbrasas
 */
abstract class Networker[A] {

  private def read(in: ObjectInputStream): Reading = try {
    in.readObject() match {
      case data: A => Successful(data)
      case _ => Empty()
    }
  } catch {
    case e: IOException => Failed(e)
  }

  protected def receive(socket: Socket): Reading = {
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

trait Reading

case class Successful[A](data: A) extends Reading

case class Failed(e: Throwable) extends Reading

case class Empty() extends Reading