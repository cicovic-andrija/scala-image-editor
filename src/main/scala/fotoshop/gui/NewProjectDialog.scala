package fotoshop.gui

import fotoshop.proj.{ProjectConstants, ProjectParams}
import fotoshop.util.Extensions.IntExtensions

import scala.util.Try
import scala.swing._
import scala.swing.FileChooser.SelectionMode

class NewProjectDialog(owner: Window) extends Dialog(owner) {

  title = GuiConstants.NEW_PROJ_TITLE
  visible = false
  modal = true
  resizable = false

  val inputs = List(
    new TextFieldValidator(
      GuiConstants.NEW_PROJ_NAME_LBL,
      Predef.augmentString("[a-zA-Z0-9]+").r.matches(_),
      GuiConstants.NEW_PROJ_NAME_FORMAT
    ),
    new TextFieldValidator(
      GuiConstants.NEW_PROJ_DEST_DIR_LBL,
      _.trim.nonEmpty,
      new Button(Action(GuiConstants.NEW_PROJ_CHOOSE_BTN) { publish(FolderChooserRequested()) }),
      isEnabled = false
    ),
    new TextFieldValidator(
      GuiConstants.NEW_PROJ_WIDTH_LBL,
      s => Try { s.toInt } getOrElse -1 between (ProjectConstants.OUTPUT_MIN_W_H, ProjectConstants.OUTPUT_MAX_W_H),
      GuiConstants.NEW_PROJ_SIZE_FORMAT
    ),
    new TextFieldValidator(
      GuiConstants.NEW_PROJ_HEIGHT_LBL,
      s => Try { s.toInt } getOrElse -1 between (ProjectConstants.OUTPUT_MIN_W_H, ProjectConstants.OUTPUT_MAX_W_H),
      GuiConstants.NEW_PROJ_SIZE_FORMAT
    ),
  )

  private val createButton = new Button(Action(GuiConstants.NEW_PROJ_CREATE_BTN) {
    publish(CreateProjectRequested(inputs(0).text, inputs(1).text, inputs(2).text, inputs(3).text))
  })
  createButton.enabled = false

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
    def placeholder = new Label()

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
    case e: CreateProjectRequested =>
      closeOperation()
      publish(ProjectParamsProvided(ProjectParams(e.name, e.loc, e.w, e.h)))
  }
  listenTo(inputGrid)
  owner.listenTo(this)
}
