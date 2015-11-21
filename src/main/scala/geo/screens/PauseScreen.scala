package geo.screens

import javax.swing.JLabel

import geo.Geo

/**
  * Created by paulius on 21/11/2015.
  */
class PauseScreen(_engine: Geo) extends Screen {

  val pauseText = new JLabel("PAUSED")
  add(pauseText)

  override def tick(delta: Double): Unit = {

  }

  override var engine: Geo = _engine
}
