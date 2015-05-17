package geo.structure

/**
 * @author Oliver Lea
 */
object QuadTreeNodeType extends Enumeration {
  type NodeType = Value
  val EMPTY, REFERENCE, LEAF = Value
}
