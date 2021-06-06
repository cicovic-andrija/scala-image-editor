package fotoshop.gui

import fotoshop.proj._
import fotoshop.proj.ProjectConstants._
import fotoshop.gui.GuiComponents._
import fotoshop.util.Extensions.IntExtensions

import java.awt.image.BufferedImage
import scala.swing._
import scala.util._
import scala.swing.event._
import java.io.File
import javax.imageio.ImageIO
import javax.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.Dialog._

class ApplicationFrame private extends MainFrame {

  title = GuiConstants.FRAME_TITLE
  resizable = true
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  menuBar = MyMenuBar.instance
  contents = mainPanel
  centerOnScreen()
  val newProjectDialog = new NewProjectDialog(owner = this)

  def withConfirmation(f: => Unit) {
    Project.instance match {
      case Some(project) if project.dirty =>
        Dialog.showConfirmation(
          this,
          GuiConstants.CLOSE_CONFIRM_MSG,
          GuiConstants.CLOSE_CONFIRM_TITLE,
          Options.YesNo,
          Message.Question,
          UIManager.getIcon("OptionPane.warningIcon")
        ) match {
          case Result.Yes => f
          case Result.No =>
        }
      case _ => f
    }
  }

  def withOpenProject(f: Project => Unit) {
    Project.instance match {
      case Some(project) => f(project)
      case None =>
    }
  }

  // After user gives input on the inputsPanel, the focus remains there,
  // so programmatically regain focus in some situations with this function.
  def withRegainedFocus(f: => Unit) {
    mainPanel.requestFocus()
    f
  }

  reactions += {
    case NewProjectRequested() => newProjectDialog.visible = true
    case OpenRequested() => openProject()
    case CloseRequested() => withConfirmation { closeProject() }
    case SaveRequested() => withOpenProject { saveProject }
    case SaveImageRequested() => withOpenProject { saveImage }
    case LoadImageRequested() => withOpenProject { loadImage }
    case DeleteLayersRequested() => withOpenProject { deleteLayers }
    case ToggleGuidelineRequested() => withOpenProject { toggleGuideline }
    case ToggleToolsRequested() => withRegainedFocus { inputsPanel.toggle() }
    case ToggleShortcutsRequested() => shortcutsPanel.toggle()
    case LayerToggled() => workspacePanel.update()
    case VersionRequested() => Dialog.showMessage(this, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIALOG_TITLE)
    case ExitRequested() => closeOperation()
    case InversionRequested() => withRegainedFocus { operationHandler(OP_REV_SUB, Input(PIXEL_COLOR_MAX_VAL, Set[String](RGB_R, RGB_G, RGB_B))) }
    case GrayscaleRequested() => withRegainedFocus { operationHandler(OP_GRAYSCALE, Input.Empty) }
    case OperationRequested(_, InputProvided(false)) => GuiComponents.statusBar.setErrorText(GuiConstants.SB_TEXT_INVALID_INPUT)
    case OperationRequested(op, MultiInputProvided(input @ _)) => withRegainedFocus { operationHandler(op, input) }
    case ProjectParamsProvided(params) => openNewProject(params)
    case KeyPressed(_, key, mod, _) => keyHandler(key, mod)
  }

  deafTo(this)
  mainPanel.requestFocus()
  listenTo(mainPanel.keys)
  listenTo(MyMenuBar.instance)
  listenTo(LayerList.instance)

  peer.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
  override def closeOperation() {
    withConfirmation { publish(ExitRequested()) }
  }

  def keyHandler(key: Key.Value, mod: Key.Modifiers) {
    Project.instance match {
      case Some(project) => key match {
        case Key.Up if mod mask Key.Modifier.Control =>
          moveLayerUp(project)

        case Key.Down if mod mask Key.Modifier.Control =>
          moveLayerDown(project)

        case Key.Left if mod mask Key.Modifier.Control =>
          project forSelectedLayers { _.updateTransparency(-GuiConstants.TRANSPARENCY_INCR) }

        case Key.Right if mod mask Key.Modifier.Control =>
          project forSelectedLayers { _.updateTransparency(GuiConstants.TRANSPARENCY_INCR) }

        case Key.Left if mod == GuiConstants.NO_KEY_MODIFIER =>
          project forSelectedLayers { _.moveOnX(-GuiConstants.MOVE_INCR) }

        case Key.Right if mod == GuiConstants.NO_KEY_MODIFIER =>
          project forSelectedLayers { _.moveOnX(GuiConstants.MOVE_INCR) }

        case Key.Up if mod == GuiConstants.NO_KEY_MODIFIER =>
          project forSelectedLayers { _.moveOnY(-GuiConstants.MOVE_INCR) }

        case Key.Down if mod == GuiConstants.NO_KEY_MODIFIER =>
          project forSelectedLayers { _.moveOnY(GuiConstants.MOVE_INCR) }

        case Key.Escape =>
          project forSelectedLayers { _.selected = false }
          LayerList.instance.refreshBorders()

        case _ =>
      }
      case None => return
    }
    LayerList.instance.repaint()
    workspacePanel.update()
  }

  def operationHandler(op: String, input: Input) {
    Project.instance match {
      case Some(project) =>
        project.operationHandler(op, input.C, input.rgbFlags)
        LayerList.instance.repaint()
        workspacePanel.update()
      case None =>
    }
  }

  def openNewProject(params: ProjectParams) {
    val projectFile = new File(params.location, params.name + EXT_XML)
    Try { Project.saveNew(params, projectFile) } match {
      case Success(_) => openProject(projectFile)
      case Failure(_) => GuiComponents.statusBar.setErrorText(GuiConstants.SB_FMT_NEW_PROJ_FAIL.format(params.name))
    }
  }

  def openProject(file: File) {
    Try { Project.load(file) } match {
      case Success(_) =>
        Project.instance match {
          case Some(_) =>
            LayerList.instance.reload()
            LayerList.instance.repaint()
            workspacePanel.reset()
            inputsPanel.reset()
            MyMenuBar.instance.reset()
            updateApplicationTitle()
            statusBar.clear()
          case None =>
            statusBar.setErrorText(GuiConstants.SB_FMT_CORRUPTED_PROJ.format(file.getPath))
        }
      case Failure(_) =>
        statusBar.setErrorText(GuiConstants.SB_FMT_CORRUPTED_PROJ.format(file.getPath))
    }
  }

  def openProject() {
    val fileChooser = new FileChooser() {
      title = GuiConstants.OPEN_DIALOG_TITLE
      multiSelectionEnabled = false
      fileFilter = new FileNameExtensionFilter(
        GuiConstants.OPEN_DIALOG_FILE_DESC,
        GuiConstants.EXT_XML
      )
    }
    fileChooser.showOpenDialog(this)
    if (fileChooser.selectedFile != null) {
      openProject(fileChooser.selectedFile)
    }
  }

  def saveProject(project: Project) {
    Try { project.save() } match {
      case Success(_) => statusBar.setText(GuiConstants.SB_FMT_SAVE_SUCCEEDED.format(project.filePath))
      case Failure(_) => statusBar.setErrorText(GuiConstants.SB_FMT_SAVE_FAILED.format(project.filePath))
    }
  }

  def closeProject() {
    Project.close()
    LayerList.instance.reload()
    LayerList.instance.repaint()
    workspacePanel.reset()
    inputsPanel.reset()
    MyMenuBar.instance.reset()
    updateApplicationTitle()
    statusBar.setText(GuiConstants.SB_TEXT_PROJ_CLOSED)
  }

  def loadImage(project: Project) {
    val fileChooser = new FileChooser() {
      title = GuiConstants.LOAD_IMG_DIALOG_TITLE
      multiSelectionEnabled = false
      fileFilter = new FileNameExtensionFilter(
        GuiConstants.LOAD_IMG_FILE_DESC,
        GuiConstants.EXT_JPG,
        GuiConstants.EXT_JPEG,
        GuiConstants.EXT_PNG
      )
    }

    fileChooser.showOpenDialog(this)
    if (fileChooser.selectedFile != null) {
      Try { project.loadImage(fileChooser.selectedFile.getPath) } match {
        case Success(layer) =>
          LayerList.instance.addLayer(layer)
          LayerList.instance.repaint()
          workspacePanel.update()
        case Failure(_) =>
          statusBar.setErrorText(GuiConstants.SB_FMT_LOAD_IMG_FAIL.format(fileChooser.selectedFile.getPath))
      }
    }
  }

  def deleteLayers(project: Project) {
    project.deleteSelectedLayers()
    LayerList.instance.reload()
    LayerList.instance.repaint()
    workspacePanel.update()
  }

  def toggleGuideline(project: Project): Unit = {
    project.toggleGuideline()
    workspacePanel.update()
  }

  def moveLayerUp(project: Project) {
    if (project.moveLayerUp()) {
      LayerList.instance.reload()
    } else {
      statusBar.setErrorText(GuiConstants.SB_OP_NOT_SUPPORTED)
    }
  }

  def moveLayerDown(project: Project) {
    if (project.moveLayerDown()) {
      LayerList.instance.reload()
    } else {
      statusBar.setErrorText(GuiConstants.SB_OP_NOT_SUPPORTED)
    }
  }

  def saveImage(project: Project) {
    if (project.guideline) project.toggleGuideline()
    val image = new BufferedImage(
      project.output.width,
      project.output.height,
      BufferedImage.TYPE_INT_RGB
    )
    val g2d: Graphics2D = image.createGraphics()
    workspacePanel.workspace.peer.print(g2d)
    Try { ImageIO.write(image, ProjectConstants.JPG_FORMAT_NAME, new File(project.output.imagePath)) } match {
      case Success(_) => statusBar.setText(GuiConstants.SB_FMT_IMG_SAVE_SUCC.format(project.output.imagePath))
      case Failure(_) => statusBar.setErrorText(GuiConstants.SB_TEXT_IMG_SAVE_FAIL)
    }
    g2d.dispose()
  }

  def updateApplicationTitle() {
    Project.instance match {
      case Some(project) => title = GuiConstants.FRAME_TITLE + " - " + project.name
      case None => title = GuiConstants.FRAME_TITLE
    }
  }
}

object ApplicationFrame {
  private val _instance = new ApplicationFrame()
  def instance = _instance
}
