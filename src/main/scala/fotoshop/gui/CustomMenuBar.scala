package fotoshop.gui

import scala.swing._
import scala.swing.event.Key

class CustomMenuBar private extends MenuBar with DeafToSelf {
  // Help method
  private def newMenuItem(title: String, key: Key.Value, event: MenuBarEvent): MenuItem =
    new MenuItem(Action(title){ publish(event) }) {
      mnemonic = key
      preferredSize = new Dimension(GuiConstants.MI_WIDTH, peer.getPreferredSize.height)
    }

  // Have to do it this way because Swing is retarded.
  // See Note on contents length below.
  private val _saveMenuItem = newMenuItem(GuiConstants.MI_SAVE_PROJECT, Key.S, SaveRequested())
  private val _closeMenuItem = newMenuItem(GuiConstants.MI_CLOSE_PROJECT, Key.C, CloseRequested())

  // Project menu
  contents += new Menu(GuiConstants.MENU_PROJECT) {
    mnemonic = Key.P
    contents += newMenuItem(GuiConstants.MI_NEW_PROJECT, Key.N, NewProjectRequested())
    contents += newMenuItem(GuiConstants.MI_OPEN_PROJECT, Key.O, OpenRequested())
    contents += _saveMenuItem
    contents += _closeMenuItem
    contents += new Separator()
    contents += newMenuItem(GuiConstants.MI_EXIT, Key.X, ExitRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  // Help menu
  contents += new Menu(GuiConstants.MENU_HELP) {
    mnemonic = Key.H
    contents += newMenuItem(GuiConstants.MI_TOOLS, Key.T, ToggleToolsRequested())
    contents += newMenuItem(GuiConstants.MI_SHORTCUTS, Key.S, ToggleShortcutsRequested())
    contents += newMenuItem(GuiConstants.MI_VERSION, Key.V, VersionRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  // Subscribe to all events from the menu bar
  reactions += {
    case e: MenuBarEvent => publish(e)
  }
  contents foreach { listenTo(_) }

  def enableSave() {
    _saveMenuItem.enabled = true
  }

  def disableSave() {
    _saveMenuItem.enabled = false
  }

  def enableClose() {
    _closeMenuItem.enabled = true
  }

  def disableClose() {
    _closeMenuItem.enabled = false
  }

  disableSave()
  disableClose()
}

object CustomMenuBar {
  private val _instance = new CustomMenuBar()
  def instance = _instance
}
