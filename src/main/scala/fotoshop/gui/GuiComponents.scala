package fotoshop.gui

// FIXME: Fix all imports in whole project when done.
import javax.swing.border.{BevelBorder, LineBorder, TitledBorder}
import scala.swing._
import scala.swing.BorderPanel.Position._

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

  val sidebarPanel=  new BorderPanel {
    border = GuiComponents.defaultBorder
    preferredSize = new Dimension(GuiConstants.SIDEBAR_WIDTH, peer.getPreferredSize.height)
    minimumSize = new Dimension(GuiConstants.SIDEBAR_WIDTH, peer.getPreferredSize.height)
    layout(GuiComponents.layersPanel) = Center
    layout(GuiComponents.toolsPanel) = North
    layout(GuiComponents.shortcutsPanel) = South
  }

  val statusBar: BorderPanel = new BorderPanel {
    border = new BevelBorder(BevelBorder.LOWERED)
    val label = new Label(GuiConstants.SB_INIT_TEXT) {
      horizontalAlignment = Alignment.Left
    }
    layout(label) = Center
  }
}
