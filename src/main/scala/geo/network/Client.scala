package geo.network

import java.net.Socket

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Paulius Imbrasas
 */
class Client[A] extends Networker[A] {

  val socket = new Socket("localhost", 1201)

  def send(newPacket: A): Unit = Future {
    super.send(newPacket)(socket)
  }

  //  def send(player: A) = {
  //    val socket = new Socket("localhost", 1201)
  //    val out = new ObjectOutputStream(socket.getOutputStream)
  //    val in = new ObjectInputStream(socket.getInputStream)
  //
  //    out.writeObject(player)
  //
  //    out.close()
  //    in.close()
  //    socket.close()
  //  }
}
