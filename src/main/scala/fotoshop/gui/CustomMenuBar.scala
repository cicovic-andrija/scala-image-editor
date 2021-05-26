package fotoshop.gui

import scala.swing.event.{Event, Key}
import scala.swing._

class CustomMenuBar private extends MenuBar with DeafToSelf {
  // Help method
  private def newMenuItem(title: String, key: Key.Value, event: MenuBarEvent): MenuItem =
    new MenuItem(Action(title){ publish(event) }) {
      mnemonic = key
      preferredSize = new Dimension(GuiConstants.MI_WIDTH, peer.getPreferredSize.height)
    }

  // Project menu
  contents += new Menu(GuiConstants.MENU_PROJECT) {
    mnemonic = Key.P
    contents += newMenuItem(GuiConstants.MI_NEW_PROJECT, Key.N, NewProjectRequested())
    contents += newMenuItem(GuiConstants.MI_OPEN_PROJECT, Key.O, OpenProject())
    contents += newMenuItem(GuiConstants.MI_SAVE_PROJECT, Key.S, SaveProject())
    contents += newMenuItem(GuiConstants.MI_CLOSE_PROJECT, Key.C, CloseProject())
    contents += new Separator()
    contents += newMenuItem(GuiConstants.MI_EXIT, Key.X, ExitRequested())
  }

  // Help menu
  contents += new Menu(GuiConstants.MENU_HELP) {
    mnemonic = Key.H
    contents += newMenuItem(GuiConstants.MI_TOOLS, Key.T, ToggleTools())
    contents += newMenuItem(GuiConstants.MI_SHORTCUTS, Key.S, ToggleShortcuts())
    contents += newMenuItem(GuiConstants.MI_VERSION, Key.V, ShowVersion())
  }

  // Subscribe to all events from the menu bar
  reactions += {
    case e: MenuBarEvent => publish(e);
  }
  contents foreach { listenTo(_) }
}

object CustomMenuBar {
  private val _instance = new CustomMenuBar()
  def instance = _instance
}
