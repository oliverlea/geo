package geo.network

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

import geo.domain.GPoint

/**
 * @author Paulius Imbrasas
 */
object Client {

  def ping = {
    val socket = new Socket("localhost", 1201)
    val out = new ObjectOutputStream(socket.getOutputStream)
    val in = new ObjectInputStream(socket.getInputStream)

    out.writeObject(new GPoint(0, 1))

    out.close()
    in.close()
    socket.close()
  }

  def main(args: Array[String]) {
    ping
  }
}
