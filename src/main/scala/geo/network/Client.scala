package geo.network

import java.io.{IOException, ObjectOutputStream}
import java.net.Socket

/**
 * @author Paulius Imbrasas
 */
protected class Client(private val createPacket: () => Packet) extends Networker {

  private var socket: Socket = _
  private var ostream: ObjectOutputStream = _
  private var connected = false

  def isConnected = connected

  def send(): Unit = try {
    send(ostream, createPacket())
  } catch {
    case e: IOException =>
      connected = false
      ostream.close()
  }

  def connect(ip: String): Unit = {
    if (!connected) {
      try {
        socket = new Socket(ip, 1399)
        ostream = new ObjectOutputStream(socket.getOutputStream)
        connected = true
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }

  def disconnect(): Unit = {
    ostream.close()
    connected = false
  }

}
