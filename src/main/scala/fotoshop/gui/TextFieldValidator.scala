package fotoshop.gui

import scala.swing._
import scala.swing.event.EditDone

class TextFieldValidator(labelText: String,
                         validator: String => Boolean,
                         hintComp: Component,
                         isEnabled: Boolean) extends TextField {

  columns = GuiConstants.TEXT_FIELD_WIDTH
  enabled = isEnabled

  private var _valid = false
  def valid = _valid

  private val _hint = hintComp
  def hint = _hint

  private val _label = new Label(labelText) { horizontalAlignment = Alignment.Right }
  def label = _label

  def this(labelText: String, validator: String => Boolean, hintText: String) =
    this(labelText, validator, new Label(hintText) { horizontalAlignment = Alignment.Right }, true)

  def this(labelText: String, validator: String => Boolean) =
    this(labelText, validator, new Label("") { horizontalAlignment = Alignment.Right }, true)

  def setText(t: String) {
    text = t
    publish(EditDone(this))
  }

  def reset() {
    text = ""
    _valid = false
    border = if (enabled) GuiComponents.blackBorder else GuiComponents.defaultBorder
  }
  reset()

  reactions += {
    case EditDone(s) =>
      _valid = validator(s.text)
      border = if (valid) if (enabled) GuiComponents.blackBorder else GuiComponents.defaultBorder else GuiComponents.redBorder
      publish(InputProvided(valid))
  }
  listenTo(this)
}
