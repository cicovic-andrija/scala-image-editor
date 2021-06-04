package fotoshop.gui

import fotoshop.proj.ProjectConstants._
import fotoshop.proj._
import fotoshop.util.Extensions.IntExtensions

import java.io.File
import scala.swing._
import scala.util._
import scala.swing.event._
import javax.swing.filechooser.FileNameExtensionFilter

class ApplicationFrame private extends MainFrame {

  title = GuiConstants.FRAME_TITLE
  resizable = true
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  menuBar = MyMenuBar.instance
  contents = GuiComponents.mainPanel
  centerOnScreen()

  val newProjectDialog = new NewProjectDialog(owner = this)

  reactions += {
    case _: NewProjectRequested => newProjectDialog.visible = true
    case _: OpenRequested => openProject()
    case _: CloseRequested => closeProject()
    case _: SaveRequested => saveProject()
    case _: ToggleToolsRequested => GuiComponents.toolsPanel.toggle()
    case _: ToggleShortcutsRequested => GuiComponents.shortcutsPanel.toggle()
    case _: VersionRequested => Dialog.showMessage(this, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIALOG_TITLE)
    case _: LayerToggled => GuiComponents.workspacePanel.repaint()
    case params: ProjectParams => newProject(params)
    case e: ExitRequested => publish(e)
    case KeyPressed(_, key, mod, _) => keyHandler(key, mod)
  }

  // register to listen to all events from static components
  deafTo(this)
  GuiComponents.mainPanel.requestFocus()
  listenTo(GuiComponents.mainPanel.keys)
  listenTo(MyMenuBar.instance)
  listenTo(LayerList.instance)

  def keyHandler(key: Key.Value, mod: Key.Modifiers) {
    Project.instance match {
      case Some(p) => key match {
        case Key.O => loadImage(p)
        case Key.S => saveOutput(p)
        case Key.D => deleteLayers(p)
        case Key.Up if mod mask Key.Modifier.Control => moveUp(p)
        case Key.Down if mod mask Key.Modifier.Control => moveDown(p)
        case _ =>
      }
      case None => // do nothing
    }
  }

  def newProject(params: ProjectParams) {
    val projFile = new File(params.location, params.name + EXT_XML)
    Project.saveNew(params, projFile) match {
      case Success(_) => openProject(projFile)
      case Failure(_) => GuiComponents.statusBar.setErrorText(GuiConstants.SB_FMT_NEW_PROJ_FAIL.format(params.name))
    } // FIXME: finally close file?
  }

  def openProject(file: File) {
    try {
      Project.load(file)
      Project.instance match {
        case Some(_) =>
          GuiComponents.workspacePanel.refresh()
          GuiComponents.layersPanel.refresh()
          MyMenuBar.instance.refresh()
          refreshTitle()
          GuiComponents.statusBar.clear()
        case None => throw new Exception() // Project.load failed without throwing an exception.
      }
    } catch {
      case _: Throwable => GuiComponents.statusBar.setErrorText(
        GuiConstants.SB_FMT_CORRUPTED_PROJ.format(file.getPath)
      ) // FIXME: finally close file?
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
      case Some(p) => Try(p.save()) match {
        case Success(_) => GuiComponents.statusBar.setText(GuiConstants.SB_FMT_SAVE_SUCCEEDED.format(p.filePath))
        case Failure(_) => GuiComponents.statusBar.setErrorText(GuiConstants.SB_FMT_SAVE_FAILED.format(p.filePath))
      }
      case None => // should never happen
    }
  }

  def closeProject() {
    Project.close() // FIXME: match against Project.instance?
    GuiComponents.workspacePanel.refresh()
    GuiComponents.layersPanel.refresh()
    MyMenuBar.instance.refresh()
    refreshTitle()
    GuiComponents.statusBar.setText(GuiConstants.SB_TEXT_PROJ_CLOSED)
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
      Try(project.loadImage(fileChooser.selectedFile.getPath)) match {
        case Success(layer) =>
          LayerList.instance.addLayerPanel(layer)
          GuiComponents.workspacePanel.repaint()
        case Failure(_) =>
          GuiComponents.statusBar.setErrorText(GuiConstants.SB_LOAD_IMG_FAILED)
      }
    }
  }

  def deleteLayers(project: Project) {
    project.deleteSelectedLayers()
    GuiComponents.layersPanel.refresh()
    GuiComponents.workspacePanel.repaint()
  }

  def moveUp(project: Project) = {
    if (!project.moveLayerUp()) {
      GuiComponents.statusBar.setErrorText(GuiConstants.SB_OP_NOT_SUPPORTED)
    } else {
      GuiComponents.layersPanel.refresh()
      GuiComponents.workspacePanel.repaint()
    }
  }

  def moveDown(project: Project) = {
    if (!project.moveLayerDown()) {
      GuiComponents.statusBar.setErrorText(GuiConstants.SB_OP_NOT_SUPPORTED)
    } else {
      GuiComponents.layersPanel.refresh()
      GuiComponents.workspacePanel.repaint()
    }
  }

  def saveOutput(project: Project) {
//    val image = new BufferedImage(GuiComponents.workspacePanel.size.width,
//      GuiComponents.workspacePanel.size.height,
//      BufferedImage.TYPE_INT_RGB)
//    val g2d: Graphics2D = image.createGraphics()
//    GuiComponents.workspacePanel.peer.print(g2d)
//    ImageIO.write(image, "JPG", new File("test.jpg"))
//    g2d.dispose()
  }

  def refreshTitle() {
    Project.instance match {
      case None => title = GuiConstants.FRAME_TITLE
      case Some(p) => title = GuiConstants.FRAME_TITLE + " - " + p.name
    }
  }
}

object ApplicationFrame {
  private val _instance = new ApplicationFrame()
  def instance = _instance
}
