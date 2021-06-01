package fotoshop.gui

import fotoshop.proj.Project

import javax.swing.border.TitledBorder
import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked

class ApplicationFrame private extends scala.swing.MainFrame {
  title = GuiConstants.FRAME_TITLE
  resizable = true
  // FIXME: Needed for centerOnScreen() to work(?).
  size = GuiConstants.FRAME_PREF_SIZE
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  centerOnScreen()
  menuBar = CustomMenuBar.instance
  contents = new BorderPanel() {
    // no border
    layout(GuiComponents.workspacePanel) = Center
    layout(GuiComponents.sidebarPanel) = East
    layout(GuiComponents.statusBar) = South
  }

  val newProjectDialog = new NewProjectDialog(this)

  listenTo(CustomMenuBar.instance)
  listenTo(newProjectDialog)

  deafTo(this)
  reactions += {
    case _: NewProjectRequested => newProject()
    case _: OpenRequested => openProject()
    case _: CloseRequested => closeProject()
    case _: SaveRequested => saveProject()
    case _: ToggleToolsRequested => GuiComponents.toolsPanel.toggle()
    case _: ToggleShortcutsRequested => GuiComponents.shortcutsPanel.toggle()
    case _: VersionRequested => Dialog.showMessage(this, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIALOG_TITLE)
    case e: CustomEvent => publish(e)
  }

  def newProject(): Unit = {
    newProjectDialog.visible = true
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

    fileChooser.showOpenDialog(null)
    if (fileChooser.selectedFile != null) try {
        Project.load(fileChooser.selectedFile)
        Project.instance match {
          case Some(_) => {
            GuiComponents.workspacePanel.refresh()
            GuiComponents.layersPanel.refresh()
            CustomMenuBar.instance.refresh()
            refreshTitle()
            GuiComponents.statusBar.clear()
          }
          case None => throw new Exception() // Project.load failed without throwing an exception.
        }
    } catch {
        case _: Throwable => GuiComponents.statusBar.setErrorText(
            GuiConstants.SB_FMT_CORRUPTED_PROJ.format(fileChooser.selectedFile.getPath)
          )
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
    CustomMenuBar.instance.refresh()
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
