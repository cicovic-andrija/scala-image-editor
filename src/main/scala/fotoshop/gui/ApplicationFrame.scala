package fotoshop.gui

import fotoshop.cfg.Project

import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing.{Dialog, FileChooser}
import scala.xml.XML

class ApplicationFrame private extends scala.swing.MainFrame {
  title = GuiConstants.FRAME_TITLE
  resizable = true
  // FIXME: Needed for centerOnScreen() to work(?).
  size = GuiConstants.FRAME_PREF_SIZE
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  centerOnScreen()
  menuBar = CustomMenuBar.instance
  contents = new MainPanel()

  listenTo(CustomMenuBar.instance)
  deafTo(this)
  reactions += {
    case _: OpenProject => openProject()
    case _: CloseProject => closeProject()
    case _: ToggleTools => GuiComponents.toolsPanel.toggle()
    case _: ToggleShortcuts => GuiComponents.shortcutsPanel.toggle()
    case _: ShowVersion => Dialog.showMessage(null, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIAG_TITLE)
    case e: CustomEvent => publish(e)
  }

  def openProject() {
    val fileChooser = new FileChooser() {
      title = GuiConstants.OPEN_DIAG_TITLE
      multiSelectionEnabled = false
      fileFilter = new FileNameExtensionFilter(
        GuiConstants.OPEN_DIAG_FILE_DESC,
        GuiConstants.EXT_XML,
        GuiConstants.EXT_XML
      )
    }

    fileChooser.showOpenDialog(null)
    if (fileChooser.selectedFile != null) {
      Project.load(XML.loadFile(fileChooser.selectedFile)) // FIXME: Error handling.
      title = GuiConstants.FRAME_TITLE + " - " + Project.instance.name
    }
  }

  def closeProject() {
    Project.close()
    title = GuiConstants.FRAME_TITLE
  }
}

object ApplicationFrame {
  private val _instance = new ApplicationFrame()
  def instance = _instance
}
