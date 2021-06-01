package fotoshop.gui

import scala.swing._
import scala.swing.BorderPanel.Position._

class PrettyGridPanel(gridPanel: GridPanel) extends BorderPanel {
  val gapSize = 10
  layout(Swing.VStrut(gapSize)) = North
  layout(Swing.VStrut(gapSize)) = South
  layout(Swing.HStrut(gapSize)) = East
  layout(Swing.HStrut(gapSize)) = West
  layout(gridPanel) = Center
}
