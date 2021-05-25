package fotoshop.gui

import fotoshop.cfg.Project

import javax.swing.border.{LineBorder, TitledBorder}
import javax.swing.filechooser.FileNameExtensionFilter
import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.xml.XML

object GuiComponents {
  type ToggleablePanel = BorderPanel with Toggleable

  val defaultBorder: LineBorder = new LineBorder(GuiConstants.DEFAULT_BORDER_C)

  val layersPanel: BorderPanel = new BorderPanel {
    border = new TitledBorder(GuiConstants.TB_LAYERS)
  }

  val toolsPanel: ToggleablePanel = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_TOOLS)
    visible = true
    layout(new Button("A")) = Center
  }

  val shortcutsPanel: ToggleablePanel = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_SHORTCUTS)
    preferredSize = new Dimension(peer.getPreferredSize.width, GuiConstants.SHR_TABLE_HEIGHT)
    maximumSize = new Dimension(peer.getMaximumSize.width, GuiConstants.SHR_TABLE_HEIGHT)
    visible = false
    layout(ShortcutsTable.instance) = Center
  }

  def openProject(): Option[String] = {
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
      return Some(Project.instance.name)
    }

    return None
  }

  def closeProject() {
    Project.close()
  }
}
