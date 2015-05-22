package geo.network

import java.io.{IOException, ObjectInputStream}
import java.net.{ServerSocket, Socket}

/**
 * @author Paulius Imbrasas
 */
protected class Server(private val handlePacket: Option[Packet] => Unit, private val connected: String => Unit) extends Networker with Runnable {

  override def run() = {
    try {
      val server = new ServerSocket(1399)

      println("Awaiting connection...")
      val socket: Socket = server.accept()
      val input = new ObjectInputStream(socket.getInputStream)

      connected(socket.getInetAddress.getHostAddress)

      println(s"Received connection! $socket")

      while (true) handlePacket(receive(input))

      input.close()
      server.close()
      println("Closing server")
    } catch {
      case e: IOException => println(s"Failed to open server socket: ${e.getMessage}")
    }
  }
}