package fotoshop.gui

import scala.swing._
import scala.swing.event.EditDone

class TextFieldValidator(validateInput: String => Boolean, isEnabled: Boolean) extends TextField {
  columns = GuiConstants.TEXT_FIELD_WIDTH
  enabled = isEnabled
  border = if (enabled) GuiComponents.blackBorder else GuiComponents.defaultBorder

  def this(validator: String => Boolean) = this(validator, true)

  def setValidBorderColor() {
    border = if (enabled) GuiComponents.blackBorder else GuiComponents.defaultBorder
  }
  setValidBorderColor()

  listenTo(this)
  reactions += {
    case EditDone(s) => {
      if (!validateInput(s.text)) {
        border = GuiComponents.redBorder
      } else {
        setValidBorderColor()
        publish(InputProvided(s.text))
      }
    }
  }
}
