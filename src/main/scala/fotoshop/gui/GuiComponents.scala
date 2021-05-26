package fotoshop.gui

import fotoshop.cfg.Project

// FIXME: Fix all imports in whole project when done.
import javax.swing.border.{BevelBorder, LineBorder, TitledBorder}
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
  }

  val shortcutsPanel: ToggleablePanel = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_SHORTCUTS)
    preferredSize = new Dimension(peer.getPreferredSize.width, GuiConstants.SHR_TABLE_HEIGHT)
    maximumSize = new Dimension(peer.getMaximumSize.width, GuiConstants.SHR_TABLE_HEIGHT)
    visible = false
    layout(ShortcutsTable.instance) = Center
  }

  val statusBar: BorderPanel = new BorderPanel {
    border = new BevelBorder(BevelBorder.LOWERED)
    val label = new Label(GuiConstants.SB_INIT_TEXT) {
      horizontalAlignment = Alignment.Left
    }
    layout(label) = Center
  }
}
