package geo.network

/**
 * An abstraction to handle hide whether this is the server or the client
 *
 * @tparam A type of thing that will be sent and received over the network
 * @author Paulius Imbrasas
 */
class NetworkHelper[A](val networkMode: NetworkMode) {

  // Constructor

  lazy val server = new Server[A]
  lazy val client = new Client[A]

  networkMode match {
    case ServerMode() => server.run()
    case ClientMode() =>
  }

  // Methods

  def send(packet: A) = networkMode match {
    case ServerMode() => server.packet = packet
    case ClientMode() => client.send(packet)
  }

  def receive(): A = ???
}
