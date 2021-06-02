package fotoshop.gui

import fotoshop.proj.ProjectParams

import java.io.File
import java.nio.file.Paths
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.FileChooser.SelectionMode
import scala.swing._

class NewProjectDialog private[gui](owner: Window) extends Dialog(owner) {
  title = GuiConstants.NEW_PROJ_TITLE
  visible = false
  modal = true
  resizable = false

  def placeholder = new Label()

  def validateName(s: String): Boolean = {
    s == "andrija"
  }

  def validateSize(s: String): Boolean = {
    true
  }

  val inputs = List(
    new TextFieldValidator(GuiConstants.NEW_PROJ_NAME_LBL, validateName, GuiConstants.NEW_PROJ_NAME_FORMAT),
    new TextFieldValidator(GuiConstants.NEW_PROJ_DEST_DIR_LBL,
                           _ => true,
                           new Button(Action(GuiConstants.NEW_PROJ_CHOOSE_BTN) { publish(FolderChooserRequested()) }),
                           isEnabled = false),
    new TextFieldValidator(GuiConstants.NEW_PROJ_WIDTH_LBL, validateSize, GuiConstants.NEW_PROJ_SIZE_FORMAT),
    new TextFieldValidator(GuiConstants.NEW_PROJ_HEIGHT_LBL, validateSize, GuiConstants.NEW_PROJ_SIZE_FORMAT),
  )

  private val createButton = new Button(Action(GuiConstants.NEW_PROJ_CREATE_BTN) {
    publish(ProjectParamsProvided(
      ProjectParams(
        inputs(0).text,
        inputs(1).text,
        inputs(2).text,
        inputs(3).text
        )
      )
    )
  }) { enabled = false }

  override def closeOperation() {
    createButton.enabled = false
    inputs foreach { _.reset() }
    visible = false
  }

  def chooseLocation() {
    val fileChooser = new FileChooser() {
      title = GuiConstants.DEST_DIR_DIALOG_TITLE
      multiSelectionEnabled = false
      fileSelectionMode = SelectionMode.DirectoriesOnly
    }

    fileChooser.showOpenDialog(this)
    if (fileChooser.selectedFile != null) {
      inputs find { _.label.text == GuiConstants.NEW_PROJ_DEST_DIR_LBL } match {
        case Some(field) => field.setText(fileChooser.selectedFile.getPath)
        case None => // will not happen
      }
    }
  }

  val inputGrid = new GridPanel(5, 3) with DeafToSelf {
    hGap = GuiConstants.H_GAP_SIZE
    vGap = GuiConstants.V_GAP_SIZE

    // add inputs and hints
    inputs map { i =>
      contents += i.label
      contents += i
      contents += i.hint
    }

    // add create button
    contents += placeholder
    contents += createButton
    contents += placeholder

    reactions += {
      case e: InputProvided => publish(e)
    }
    contents foreach { listenTo(_) }
  }
  contents = new PrettyGridPanel(inputGrid)
  centerOnScreen()

  reactions += {
    case InputProvided(false) => createButton.enabled = false
    case InputProvided(true) => createButton.enabled = inputs.forall(_.valid)
    case _: FolderChooserRequested => chooseLocation()
    case e: ProjectParamsProvided =>
      closeOperation()
      publish(e)
  }
  listenTo(inputGrid)
}
