package fotoshop.gui

import fotoshop.proj.ProjectConstants._
import fotoshop.proj._
import fotoshop.util.Extensions.IntExtensions
import GuiComponents._

import java.awt.image.BufferedImage
import scala.swing._
import scala.util._
import scala.swing.event._
import java.io.File
import javax.imageio.ImageIO
import javax.swing.filechooser.FileNameExtensionFilter

class ApplicationFrame private extends MainFrame {

  title = GuiConstants.FRAME_TITLE
  resizable = true
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  menuBar = MyMenuBar.instance
  contents = mainPanel
  centerOnScreen()

  val newProjectDialog = new NewProjectDialog(owner = this)

  reactions += {
    case _: NewProjectRequested => newProjectDialog.visible = true
    case _: OpenRequested => openProject()
    case _: CloseRequested => closeProject()
    case _: SaveRequested => saveProject()
    case _: ToggleToolsRequested => toolsPanel.toggle()
    case _: ToggleShortcutsRequested => shortcutsPanel.toggle()
    case _: VersionRequested => Dialog.showMessage(this, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIALOG_TITLE)
    case _: LayerToggled => workspacePanel.repaint() // FIXME(?)
    case ProjectParamsProvided(params) => newProject(params)
    case e: ExitRequested => publish(e)
    case KeyPressed(_, key, mod, _) => keyHandler(key, mod)
  }

  deafTo(this)
  mainPanel.requestFocus()
  listenTo(mainPanel.keys)
  listenTo(MyMenuBar.instance)
  listenTo(LayerList.instance)

  def keyHandler(key: Key.Value, mod: Key.Modifiers) {
    Project.instance match {
      case Some(project) => key match {
        case Key.O => loadImage(project)
        case Key.S => saveOutput(project)
        case Key.D => deleteLayers(project)
        case Key.G => project.toggleGuideline(); workspacePanel.repaint()
        case Key.Up if mod mask Key.Modifier.Control => moveUp(project)
        case Key.Down if mod mask Key.Modifier.Control => moveDown(project)
        case _ =>
      }
      case None =>
    }
  }

  def newProject(params: ProjectParams) {
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
            workspacePanel.reset()
            layersPanel.refresh()
            MyMenuBar.instance.updateAvailableMenus()
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

  def saveProject() {
    Project.instance match {
      case Some(p) => Try { p.save() } match {
        case Success(_) => statusBar.setText(GuiConstants.SB_FMT_SAVE_SUCCEEDED.format(p.filePath))
        case Failure(_) => statusBar.setErrorText(GuiConstants.SB_FMT_SAVE_FAILED.format(p.filePath))
      }
      case None =>
    }
  }

  def closeProject() {
    Project.close()
    workspacePanel.reset()
    layersPanel.refresh()
    MyMenuBar.instance.updateAvailableMenus()
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
          LayerList.instance.addLayerPanel(layer)
          LayerList.instance.repaint()
          workspacePanel.repaint()
        case Failure(_) =>
          statusBar.setErrorText(GuiConstants.SB_LOAD_IMG_FAILED)
      }
    }
  }

  def deleteLayers(project: Project) {
    project.deleteSelectedLayers()
    layersPanel.refresh()
    workspacePanel.repaint()
  }

  def moveUp(project: Project) = {
    if (!project.moveLayerUp()) {
      statusBar.setErrorText(GuiConstants.SB_OP_NOT_SUPPORTED)
    } else {
      layersPanel.refresh()
      workspacePanel.repaint()
      statusBar.clear()
    }
  }

  def moveDown(project: Project) = {
    if (!project.moveLayerDown()) {
      statusBar.setErrorText(GuiConstants.SB_OP_NOT_SUPPORTED)
    } else {
      layersPanel.refresh()
      workspacePanel.repaint()
      statusBar.clear()
    }
  }

  def saveOutput(project: Project) {
    val image = new BufferedImage(
      project.output.width,
      project.output.height,
      BufferedImage.TYPE_INT_RGB
    )
    val g2d: Graphics2D = image.createGraphics()
    workspacePanel.workspace.peer.print(g2d)
    Try { ImageIO.write(image, ProjectConstants.JPG_FORMAT_NAME, new File("test.jpg")) } match {
      case Success(_) => statusBar.setText(GuiConstants.SB_FMT_IMG_SAVE_SUCC.format("test.jpg"))
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
