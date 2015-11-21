package geo.screens

import javax.swing.JPanel

import geo.Geo

/**
 * @author Paulius Imbrasas
 */
trait Screen extends JPanel {
  var engine: Geo
  def tick(delta: Double)
}
