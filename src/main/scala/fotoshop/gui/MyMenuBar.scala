package fotoshop.gui

import fotoshop.proj.Project

import scala.swing._
import scala.swing.event.Key

class MyMenuBar private extends MenuBar with DeafToSelf {

  private def newMenuItem(title: String, key: Key.Value, event: MenuBarEvent): MenuItem =
    new MenuItem(Action(title){ publish(event) }) {
      mnemonic = key
      preferredSize = new Dimension(GuiConstants.MI_WIDTH, peer.getPreferredSize.height)
    }

  // Have to do it this way because Swing maybe has a bug.
  // See Note on contents length below.
  private val _newMenuItem = newMenuItem(GuiConstants.MI_NEW_PROJECT, Key.N, NewProjectRequested())
  private val _openMenuItem = newMenuItem(GuiConstants.MI_OPEN_PROJECT, Key.O, OpenRequested())
  private val _saveMenuItem = newMenuItem(GuiConstants.MI_SAVE_PROJECT, Key.S, SaveRequested())
  private val _saveImageMenuItem = newMenuItem(GuiConstants.MI_SAVE_IMAGE, Key.I, SaveImageRequested())
  private val _closeMenuItem = newMenuItem(GuiConstants.MI_CLOSE_PROJECT, Key.C, CloseRequested())

  contents += new Menu(GuiConstants.MENU_PROJECT) {
    mnemonic = Key.P
    contents += _newMenuItem
    contents += _openMenuItem
    contents += _saveMenuItem
    contents += _saveImageMenuItem
    contents += _closeMenuItem
    contents += new Separator()
    contents += newMenuItem(GuiConstants.MI_EXIT, Key.X, ExitRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  contents += new Menu(GuiConstants.MENU_LAYER) {
    mnemonic = Key.L
    contents += newMenuItem(GuiConstants.MI_LOAD_IMAGE, Key.I, LoadImageRequested())
    contents += newMenuItem(GuiConstants.MI_DELETE_LAYERS, Key.D, DeleteLayersRequested())
    contents += newMenuItem(GuiConstants.MI_TOGGLE_GUIDELINE, Key.G, ToggleGuidelineRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  contents += new Menu(GuiConstants.MENU_HELP) {
    mnemonic = Key.H
    contents += newMenuItem(GuiConstants.MI_TOOLS, Key.T, ToggleToolsRequested())
    contents += newMenuItem(GuiConstants.MI_SHORTCUTS, Key.S, ToggleShortcutsRequested())
    contents += newMenuItem(GuiConstants.MI_VERSION, Key.V, VersionRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  def updateAvailableMenus() {
    Project.instance match {
      case None =>
        _newMenuItem.enabled = true
        _openMenuItem.enabled = true
        _saveMenuItem.enabled = false
        _saveImageMenuItem.enabled = false
        _closeMenuItem.enabled = false
        contents(1).enabled = false
      case Some(_) =>
        _newMenuItem.enabled = false
        _openMenuItem.enabled = false
        _saveMenuItem.enabled = true
        _saveImageMenuItem.enabled = true
        _closeMenuItem.enabled = true
        contents(1).enabled = true
    }
  }
  updateAvailableMenus()
}

object MyMenuBar {
  private val _instance = new MyMenuBar()
  def instance = _instance
}
