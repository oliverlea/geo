package geo.network

import java.net.{ServerSocket, Socket}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Paulius Imbrasas
 */
class Server[A] extends Networker[A] {

  private var _packet: A = _

  def packet_=(p: A): Unit = _packet = p

  def run() = Future {
    val server = new ServerSocket(1201)
    while (true) {
      val socket: Socket = server.accept()
      Future {
        val p = receive(socket) match {
          case Successful(data) => println(s"Received packet: $data")
          case Failed(e) => println(s"Error in packet: ${e.getMessage}")
          case Empty() => println("Empty packet")
        }
      }
    }
    server.close()
  }

}