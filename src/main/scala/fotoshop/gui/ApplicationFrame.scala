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

  // IDEAS: Clone layer, scale layer, confirm on exit, deselect all

  title = GuiConstants.FRAME_TITLE
  resizable = true
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  menuBar = MyMenuBar.instance
  contents = mainPanel
  centerOnScreen()

  val newProjectDialog = new NewProjectDialog(owner = this)

  def withOpenProject(f: Project => Unit) {
    Project.instance match {
      case Some(project) => f(project)
      case None =>
    }
  }

  reactions += {
    case _: NewProjectRequested => newProjectDialog.visible = true
    case _: OpenRequested => openProject()
    case _: CloseRequested => closeProject()
    case _: SaveRequested => withOpenProject { saveProject }
    case _: SaveImageRequested => withOpenProject { saveImage }
    case _: LoadImageRequested => withOpenProject { loadImage }
    case _: DeleteLayersRequested => withOpenProject { deleteLayers }
    case _: ToggleGuidelineRequested => withOpenProject { toggleGuideline }
    case _: ToggleToolsRequested => toolsPanel.toggle()
    case _: ToggleShortcutsRequested => shortcutsPanel.toggle()
    case _: LayerToggled => workspacePanel.update()
    case _: VersionRequested => Dialog.showMessage(this, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIALOG_TITLE)
    case ProjectParamsProvided(params) => openNewProject(params)
    case KeyPressed(_, key, mod, _) => keyHandler(key, mod)
    case e: ExitRequested => publish(e)
  }

  deafTo(this)
  mainPanel.requestFocus()
  listenTo(mainPanel.keys)
  listenTo(MyMenuBar.instance)
  listenTo(LayerList.instance)

  def keyHandler(key: Key.Value, mod: Key.Modifiers) {
    Project.instance match {
      case Some(project) => key match {
        case Key.Escape =>
          project.unselectAll()
          LayerList.instance.refreshBorders()
        case Key.Left if mod == GuiConstants.NO_KEY_MODIFIER => project.moveImagesOnX(-GuiConstants.MOVE_INCR)
        case Key.Right if mod == GuiConstants.NO_KEY_MODIFIER => project.moveImagesOnX(GuiConstants.MOVE_INCR)
        case Key.Up if mod == GuiConstants.NO_KEY_MODIFIER => project.moveImagesOnY(-GuiConstants.MOVE_INCR)
        case Key.Down if mod == GuiConstants.NO_KEY_MODIFIER => project.moveImagesOnY(GuiConstants.MOVE_INCR)
        case Key.Left if mod mask Key.Modifier.Control => project.updateTransparency(-GuiConstants.TRANSPARENCY_INCR)
        case Key.Right if mod mask Key.Modifier.Control => project.updateTransparency(GuiConstants.TRANSPARENCY_INCR)
        case Key.Up if mod mask Key.Modifier.Control => moveLayerUp(project)
        case Key.Down if mod mask Key.Modifier.Control => moveLayerDown(project)
        case _ =>
      }
      case None => return
    }
    LayerList.instance.repaint()
    workspacePanel.update()
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
