package geo.network

import java.net.{ServerSocket, Socket}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Paulius Imbrasas
 */
class Server[A] extends Networker[A] {

  def run() = Future {
    val server = new ServerSocket(1201)
    while (true) {
      val socket: Socket = server.accept()
      Future {
        val p = receive(socket)
        println(p)
      }
    }
    server.close()
  }

}