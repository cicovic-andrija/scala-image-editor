package fotoshop.gui

import fotoshop.proj.ProjectConstants._
import fotoshop.proj._

import java.io.File
import scala.swing._
import scala.util._
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.BorderPanel.Position._

class ApplicationFrame private extends scala.swing.MainFrame {
  title = GuiConstants.FRAME_TITLE
  resizable = true
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  menuBar = MyMenuBar.instance
  contents = new BorderPanel() {
    // no border
    layout(GuiComponents.workspacePanel) = Center
    layout(GuiComponents.sidebarPanel) = East
    layout(GuiComponents.statusBar) = South
  }
  centerOnScreen() // must be after contents is defined

  val newProjectDialog = new NewProjectDialog(this)

  listenTo(MyMenuBar.instance)
  listenTo(newProjectDialog)

  deafTo(this)
  reactions += {
    case _: NewProjectRequested => newProjectDialog.visible = true
    case _: OpenRequested => openProject()
    case _: CloseRequested => closeProject()
    case _: SaveRequested => saveProject()
    case _: ToggleToolsRequested => GuiComponents.toolsPanel.toggle()
    case _: ToggleShortcutsRequested => GuiComponents.shortcutsPanel.toggle()
    case _: VersionRequested => Dialog.showMessage(this, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIALOG_TITLE)
    case params: ProjectParams => newProject(params)
  }

  def newProject(params: ProjectParams) {
    val projFileName = params.name + EXT_XML
    val outputFileName = params.name + EXT_JPG
    val projFile = new File(params.location, projFileName)
    Project.saveNew(params, projFile) match {
      case Success(_) => openProject(projFile)
      case Failure(_) => GuiComponents.statusBar.setErrorText(GuiConstants.SB_FMT_NEW_PROJ_FAIL.format(params.name))
    }
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
      )
    }
  }

  def openProject() {
    val fileChooser = new FileChooser() {
      title = GuiConstants.OPEN_DIALOG_TITLE
      multiSelectionEnabled = false
      fileFilter = new FileNameExtensionFilter(
        GuiConstants.OPEN_DIALOG_FILE_DESC,
        GuiConstants.EXT_XML,
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
      case Some(p) => try {
        p.save()
        GuiComponents.statusBar.setText(GuiConstants.SB_FMT_SAVE_SUCCEEDED.format(p.filePath))
      } catch {
        case _: Throwable => GuiComponents.statusBar.setErrorText(GuiConstants.SB_FMT_SAVE_FAILED.format(p.filePath))
      }
      case None => // should never happen
    }
  }

  def closeProject() {
    Project.close()
    GuiComponents.workspacePanel.refresh()
    GuiComponents.layersPanel.refresh()
    MyMenuBar.instance.refresh()
    refreshTitle()
    GuiComponents.statusBar.setText(GuiConstants.SB_TEXT_PROJ_CLOSED)
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
