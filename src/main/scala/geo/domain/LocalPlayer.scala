package geo.domain

import geo.screens.GeoPanel

/**
 * @author Paulius Imbrasas
 */
class LocalPlayer(private val gp: GeoPanel,
                   private val initialVelocity: Velocity,
                   var _position: GPoint) extends Player(gp, initialVelocity, _position) {



}
