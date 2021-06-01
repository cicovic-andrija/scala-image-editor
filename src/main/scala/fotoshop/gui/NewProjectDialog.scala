package fotoshop.gui

import scala.swing._

class NewProjectDialog private[gui](owner: Window) extends Dialog(owner) {
  title = GuiConstants.NEW_PROJ_DIALOG_TITLE
  visible = false
  modal = true
  resizable = false

  def newLabel(text: String) = new Label(text) {
    horizontalAlignment = Alignment.Right
  }

  def placeholder = new Label()

  def validateName(s: String): Boolean = {
    s == "andrija"
  }

  def validateLocation(s: String): Boolean = {
    true
  }

  def validateWidthOrHeight(s: String): Boolean = {
    true
  }

  val inputs = new GridPanel(0, 3) with DeafToSelf {
    contents += new GridPanel(0, 1) {
      vGap = GuiConstants.V_GAP_SIZE
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_NAME_LABEL)
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_DEST_DIR_LABEL)
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_WIDTH_LABEL)
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_HEIGHT_LABEL)
      contents += placeholder
    }
    contents += new GridPanel(0, 1) with DeafToSelf {
      vGap = GuiConstants.V_GAP_SIZE
      contents += new TextFieldValidator(validateName)
      contents += new TextFieldValidator(validateLocation, isEnabled = false)
      contents += new TextFieldValidator(validateWidthOrHeight)
      contents += new TextFieldValidator(validateWidthOrHeight)
      contents += new Button(GuiConstants.NEW_PROJ_DIALOG_CREATE_BTN) { enabled = false }

      reactions += {
        case e: InputEvent => publish(e)
      }
      contents foreach { listenTo(_) }
    }
    contents += new GridPanel(0, 1) {
      vGap = GuiConstants.V_GAP_SIZE
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_NAME_FORMAT)
      contents += new Button(GuiConstants.NEW_PROJ_DIALOG_CHOOSE_BTN)
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_MIN_MAX_LBL)
      contents += newLabel(GuiConstants.NEW_PROJ_DIALOG_MIN_MAX_LBL)
      contents += placeholder
    }
    hGap = GuiConstants.H_GAP_SIZE

    reactions += {
      case e: InputEvent => publish(e)
    }
    contents foreach { listenTo(_) }
  }

  contents = new PrettyGridPanel(inputs)
  listenTo(inputs)
  reactions += {
    case InputProvided(t) => println(t)
  }

  centerOnScreen() // must be at the end of the constructor body for some reason
}
