package geo

import java.awt.Graphics
import javax.swing.JPanel

/**
 * @author Oliver Lea
 */
class GeoPanel extends JPanel {

  override def paintComponent(g: Graphics): Unit = {
      g.drawRect(20, 20, 20, 20)
  }
}
