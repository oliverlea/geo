package geo.network

import java.io.{IOException, ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

import geo.domain.GPoint

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Paulius Imbrasas
 */
object Server {

  def handleClient(s: Socket): Unit = {
    println("Handling client")
    val in = new ObjectInputStream(s.getInputStream)
    val out = new ObjectOutputStream(s.getOutputStream)

    def readClient: Option[GPoint] = try {
      in.readObject() match {
        case point: GPoint => Option(point)
        case _ => None
      }
    } catch {
      case _: IOException => None
    }

    readClient match {
      case Some(respond) => println(respond)
      case None =>
    }

    in.close()
    out.close()
    s.close()
  }

  def main(args: Array[String]) {
    val server = new ServerSocket(1201)
    while (true) {
      val s: Socket = server.accept()

      Future {
        handleClient(s)
      }
    }
    server.close()
  }

  //  def run() {
  //    val server = new ServerSocket(1201)
  //    while (true) {
  //      val s: Socket = server.accept()
  //      Future {
  //        handleClient(s)
  //      }
  //    }
  //    server.close()
  //  }

}
