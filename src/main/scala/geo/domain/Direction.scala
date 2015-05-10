package geo.domain

/**
 * @author Oliver Lea
 */
object Direction extends Enumeration {
  type Movement = Value
  val UP, DOWN, RIGHT, LEFT = Value
}
