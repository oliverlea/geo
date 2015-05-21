package geo.network

import java.io.ObjectOutputStream
import java.net.Socket

import geo.network.Networking.PacketOut

/**
 * @author Paulius Imbrasas
 */
protected class Client(val out: PacketOut) extends Networker {

  var socket: Option[Socket] = None
  var ostream: ObjectOutputStream = _
  var connected = false

  def isConnected = connected

  def send(): Unit = socket match {
    case Some(s) => send(ostream, out())
    case None => println("Socket closed")
  }

  def connect(): Unit = {
    if (!connected) {
      try {
        val s = new Socket("192.168.0.16", 1201)
        ostream = new ObjectOutputStream(s.getOutputStream)
        socket = Option(s)
        connected = true
      } catch {
        case e: Exception => println(e.getMessage)
      }
    }
  }

  def disconnect(): Unit = socket match {
    case Some(s) =>
      ostream.close()
      s.close()
      connected = false
    case None => println("Socket closed already")
  }

}
