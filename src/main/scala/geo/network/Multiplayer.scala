package geo.network

import java.math.BigInteger
import java.security.SecureRandom

import geo.domain.Player

/**
 * An abstraction to handle hide whether this is the server or the client
 *
 * @author Paulius Imbrasas
 */
class Multiplayer(val player: Player, val nplayer: NPlayer) {

  // Members

  private var networkCountdown: Double = 0
  private var remoteIP: String = _

  // Constructor

  val server = new Server(receivePacket, remoteIP = _)
  lazy val client = new Client(sendPacket)

  val serverThread = new Thread(server).start()

  // Methods

  def connect() = client.connect(remoteIP)

  def connect(ip: String) = client.connect(ip)

  def tick(delta: Double): Unit = {
    if (client.isConnected) {
      networkCountdown -= delta
      if (networkCountdown <= 0) {
        client.send()
        networkCountdown = Multiplayer.NetworkDelay
      }
    }
  }

  /**
   * Handles a packet when it comes in from the other player
   *
   * @param o maybe a packet, if reading was successful
   */
  def receivePacket(o: Option[Packet]): Unit = {
    o match {
      case Some(packet) => {
        nplayer.position = packet.position
        nplayer.velocity = packet.velocity
      }
      case None =>
    }
  }

  /**
   * Handles sending out a packet to the other player
   */
  def sendPacket(): Packet = new Packet(player.position, player._velocity, Multiplayer.Identifier)
}

object Multiplayer {
  val random = new SecureRandom
  val Identifier = new BigInteger(130, random).toString(32)
  val NetworkDelay = 0
}