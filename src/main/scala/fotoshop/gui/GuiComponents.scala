package fotoshop.gui

// FIXME: Fix all imports in whole project when done.
import fotoshop.proj.Project

import javax.swing.border.{BevelBorder, LineBorder, TitledBorder}
import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.ScrollPane.BarPolicy
import scala.swing.Swing.EmptyIcon

object GuiComponents {
  val defaultBorder: LineBorder = new LineBorder(GuiConstants.COLOR_LIGHT_GRAY)
  val blackBorder: LineBorder = new LineBorder(GuiConstants.COLOR_BLACK, GuiConstants.LINE_THICKNESS)

  val workspacePanel = new BorderPanel() with Refreshable {
    // no border
    val scrollPane =  new ScrollPane() {
      border = null // no border
      verticalScrollBarPolicy = BarPolicy.AsNeeded
      horizontalScrollBarPolicy = BarPolicy.AsNeeded
    }
    layout(scrollPane) = Center

    def preRefresh() {
      Project.instance match {
        case Some(p) => scrollPane.contents = new Workspace(p.output.width, p.output.height)
        case None => scrollPane.contents = null
        case _ =>
      }
    }
  }

  val layersPanel = new BorderPanel with Refreshable {
    border = new TitledBorder(GuiConstants.TB_LAYERS)
    val scrollPane = new ScrollPane() {
      border = null // no border
      verticalScrollBarPolicy = BarPolicy.AsNeeded
      horizontalScrollBarPolicy = BarPolicy.AsNeeded
      contents = LayerList.instance
    }
    layout(scrollPane) = Center

    def preRefresh() {
      LayerList.instance.refreshLayers()
    }
  }

  val toolsPanel = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_TOOLS)
    visible = true
  }

  val shortcutsPanel = new BorderPanel with Toggleable {
    border = new TitledBorder(GuiConstants.TB_SHORTCUTS)
    preferredSize = new Dimension(peer.getPreferredSize.width, GuiConstants.SHR_TABLE_HEIGHT)
    maximumSize = new Dimension(peer.getMaximumSize.width, GuiConstants.SHR_TABLE_HEIGHT)
    visible = false
    val scrollPane = new ScrollPane() {
      border = null // no border
      verticalScrollBarPolicy = BarPolicy.AsNeeded
      horizontalScrollBarPolicy = BarPolicy.AsNeeded
      contents = ShortcutsTable.instance
    }
    layout(scrollPane) = Center
  }

  val sidebarPanel = new BorderPanel {
    border = GuiComponents.defaultBorder
    preferredSize = new Dimension(GuiConstants.SIDEBAR_WIDTH, peer.getPreferredSize.height)
    minimumSize = new Dimension(GuiConstants.SIDEBAR_WIDTH, peer.getPreferredSize.height)
    layout(GuiComponents.layersPanel) = Center
    layout(GuiComponents.toolsPanel) = North
    layout(GuiComponents.shortcutsPanel) = South
  }

  val statusBar = new BorderPanel {
    border = new BevelBorder(BevelBorder.LOWERED)

    val label = new Label("", EmptyIcon, Alignment.Left)
    layout(label) = Center

    def setText(text: String) {
      label.text = text
    }

    def clear() {
      setText(GuiConstants.SB_TEXT_READY)
    }

    def setErrorText(text: String) {
      setText(GuiConstants.SB_ERROR_PREFIX + text)
    }

    setText(GuiConstants.SB_INIT_TEXT)
  }
}
