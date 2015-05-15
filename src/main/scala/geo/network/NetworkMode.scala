package geo.network

trait NetworkMode

case class ServerMode() extends NetworkMode

case class ClientMode() extends NetworkMode
