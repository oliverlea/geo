package geo.network

import java.math.BigInteger
import java.security.SecureRandom

import geo.domain.Player

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * An abstraction to handle hide whether this is the server or the client
 *
 * @author Paulius Imbrasas
 */
class Multiplayer(val player: Player, val nplayer: NPlayer) {

  // Members

  private var networkCountdown: Double = 0

  // Constructor

  val server = new Server(in)
  lazy val client = new Client(out)

  val serverThread = new Thread(server).start()

  // Methods

  def connect() = client.connect()

  def tick(delta: Double): Unit = {
    if (client.isConnected) {
      networkCountdown -= delta
      if (networkCountdown <= 0) {
        Future {
          client.send()
        }
        networkCountdown = Multiplayer.NetworkDelay
      }
    }
  }

  def in(o: Option[Packet]): Unit = {
    nplayer.position = o match {
      case Some(packet) => packet.position
      case None => nplayer.position
    }
  }

  def out(): Packet = new Packet(player.position, player.velocity, Multiplayer.Identifier)
}

object Multiplayer {
  val random = new SecureRandom
  val Identifier = new BigInteger(130, random).toString(32)
  val NetworkDelay = 5
}