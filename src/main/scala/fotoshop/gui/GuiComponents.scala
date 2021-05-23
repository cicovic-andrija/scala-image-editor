package fotoshop.gui

import scala.swing._
import javax.swing.border.{LineBorder, TitledBorder}
import scala.swing.BorderPanel.Position.Center

object GuiComponents {
  val DefaultBorder: LineBorder = new LineBorder(GuiConstants.DEFAULT_BORDER_C)

  val LayersPanel: BorderPanel = new BorderPanel {
    border = new TitledBorder(GuiConstants.TB_LAYERS)
  }

  val ToolsPanel: BorderPanel with Toggleable = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_TOOLS)
    visible = true
    layout(new Button("A")) = Center
  }

  val ShortcutsPanel: BorderPanel with Toggleable = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_SHORTCUTS)
    preferredSize = new Dimension(peer.getPreferredSize.width, GuiConstants.SHORTCUTS_TABLE_HEIGHT)
    maximumSize = new Dimension(peer.getMaximumSize.width, GuiConstants.SHORTCUTS_TABLE_HEIGHT)
    visible = false
    layout(ShortcutsTable.instance) = Center
  }
}
