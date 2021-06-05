package fotoshop.gui

// FIXME: Fix all imports in whole project when done.
import fotoshop.proj.Project

import scala.swing._
import javax.swing.border._
import scala.swing.BorderPanel.Position._
import scala.swing.ScrollPane.BarPolicy

object GuiComponents {

  val defaultBorder: LineBorder = new LineBorder(GuiConstants.COLOR_LIGHT_GRAY)
  val blackBorder: LineBorder = new LineBorder(GuiConstants.COLOR_BLACK)
  val redBorder: LineBorder = new LineBorder(GuiConstants.COLOR_RED)
  val thickBlackBorder: LineBorder = new LineBorder(GuiConstants.COLOR_BLACK, GuiConstants.LINE_THICKNESS)
  val thickRedBorder: LineBorder = new LineBorder(GuiConstants.COLOR_RED, GuiConstants.LINE_THICKNESS)
  val thickBlueBorder: LineBorder = new LineBorder(GuiConstants.COLOR_BLUE, GuiConstants.LINE_THICKNESS)
  val inputsPanel = new InputsPanel()
  val shortcutsPanel = new ShortcutsPanel()
  val statusBar = new StatusBar()

  val workspacePanel = new BorderPanel() {
    // no border
    var workspace: Workspace = _
    val scrollPane =  new ScrollPane() {
      border = null // no border
      verticalScrollBarPolicy = BarPolicy.AsNeeded
      horizontalScrollBarPolicy = BarPolicy.AsNeeded
      contents = Workspace.Empty
    }
    layout(scrollPane ) = Center

    def reset() {
      Project.instance match {
        case Some(project) =>
          workspace = new Workspace(project.output.width, project.output.height)
          scrollPane.contents = workspace
        case None =>
          workspace = null
          scrollPane.contents = Workspace.Empty
      }
      repaint()
    }

    def update() {
      if (workspace == null) return
      workspace.repaint()
    }
  }

  val layersPanel = new BorderPanel {
    border = new TitledBorder(GuiConstants.TB_LAYERS)
    val scrollPane = new ScrollPane() {
      border = null // no border
      verticalScrollBarPolicy = BarPolicy.AsNeeded
      horizontalScrollBarPolicy = BarPolicy.AsNeeded
      contents = LayerList.instance
      listenTo(LayerList.instance)
    }
    layout(scrollPane) = Center
  }


  val sidebarPanel = new BorderPanel {
    border = GuiComponents.defaultBorder
    preferredSize = new Dimension(GuiConstants.SIDEBAR_WIDTH, peer.getPreferredSize.height)
    minimumSize = new Dimension(GuiConstants.SIDEBAR_WIDTH, peer.getPreferredSize.height)
    layout(GuiComponents.layersPanel) = Center
    layout(GuiComponents.inputsPanel) = North
    layout(GuiComponents.shortcutsPanel) = South
  }

  val mainPanel = new BorderPanel() {
    // no border
    layout(workspacePanel) = Center
    layout(sidebarPanel) = East
    layout(statusBar) = South
    focusable = true
  }
}
