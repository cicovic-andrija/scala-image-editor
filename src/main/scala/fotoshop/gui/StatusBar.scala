package fotoshop.gui

import javax.swing.border.BevelBorder
import scala.swing.BorderPanel.Position.Center
import scala.swing.Swing.EmptyIcon
import scala.swing._

class StatusBar extends BorderPanel {

  border = new BevelBorder(BevelBorder.LOWERED)

  val label = new Label("", EmptyIcon, Alignment.Left)
  layout(label) = Center

  def setText(text: String) {
    label.text = text
  }

  def setErrorText(text: String) {
    setText(GuiConstants.SB_ERROR_PREFIX + text)
  }

  def clear() {
    setText(GuiConstants.SB_TEXT_READY)
  }

  setText(GuiConstants.SB_INIT_TEXT)
}
