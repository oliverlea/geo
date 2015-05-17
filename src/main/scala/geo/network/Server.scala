package geo.network

import java.io.{IOException, ObjectInputStream}
import java.net.{ServerSocket, Socket}

import geo.network.Networking.PacketIn

/**
 * @author Paulius Imbrasas
 */
protected class Server(val in: PacketIn) extends Networker with Runnable {

  override def run() = {
    try {
      val server = new ServerSocket(1201)

      println("Awaiting connection...")
      val socket: Socket = server.accept()
      val input = new ObjectInputStream(socket.getInputStream)

      println(s"Received connection! $socket")

      while (true) in(receive(input))

      input.close()
      server.close()
      println("Closing server")
    } catch {
      case e: IOException => println(s"Failed to open server socket: ${e.getMessage}")
    }
  }
}