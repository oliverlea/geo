package geo

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, Action}

/**
 * @author Paulius Imbrasas
 */
object Implicits {
  implicit def convertLambdaToAction(f: () => Any): Action = new AbstractAction() {
    override def actionPerformed(e: ActionEvent): Unit = {
      f()
    }
  }
}
