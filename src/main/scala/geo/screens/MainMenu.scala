package geo.screens

import java.awt.Graphics
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton

import geo.Geo

/**
 * @author Paulius Imbrasas
 */
class MainMenu(_engine: Geo) extends Screen {

  // Constructor

  val singleplayer = new JButton()
  singleplayer.setText("Singleplayer")
  singleplayer.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      _engine.screen = new GeoPanel(engine)
    }
  })

  add(singleplayer)

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
  }

  override def tick(delta: Double): Unit = {

  }

  override var engine: Geo = _engine
}
