package fotoshop.gui

import fotoshop.proj.{Project, ProjectConstants}
import fotoshop.gui.GuiComponents._

import scala.swing._
import scala.swing.event.Key

class MyMenuBar private extends MenuBar with DeafToSelf {

  private def menuItem(title: String, key: Key.Value, event: MenuBarEvent) =
    new MenuItem(Action(title){ publish(event) }) {
      mnemonic = key
      preferredSize = new Dimension(GuiConstants.MI_WIDTH, peer.getPreferredSize.height)
    }

  private def opRequestMenuItem(title: String, key: Key.Value, op: String) =
    new MenuItem(Action(title) { publish(OperationRequested(op, inputsPanel.gatherInput)) }) {
      mnemonic = key
      preferredSize = new Dimension(GuiConstants.MI_WIDTH, peer.getPreferredSize.height)
    }

  // Have to do it this way because Swing maybe has a bug.
  // See Note on contents length below.
  private val _newMenuItem = menuItem(GuiConstants.MI_NEW_PROJECT, Key.N, NewProjectRequested())
  private val _openMenuItem = menuItem(GuiConstants.MI_OPEN_PROJECT, Key.O, OpenRequested())
  private val _saveMenuItem = menuItem(GuiConstants.MI_SAVE_PROJECT, Key.S, SaveRequested())
  private val _saveImageMenuItem = menuItem(GuiConstants.MI_SAVE_IMAGE, Key.I, SaveImageRequested())
  private val _closeMenuItem = menuItem(GuiConstants.MI_CLOSE_PROJECT, Key.C, CloseRequested())

  contents += new Menu(GuiConstants.MENU_PROJECT) {
    mnemonic = Key.P
    contents += _newMenuItem
    contents += _openMenuItem
    contents += _saveMenuItem
    contents += _saveImageMenuItem
    contents += _closeMenuItem
    contents += new Separator()
    contents += menuItem(GuiConstants.MI_EXIT, Key.X, ExitRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  contents += new Menu(GuiConstants.MENU_LAYER) {
    mnemonic = Key.L
    contents += menuItem(GuiConstants.MI_LOAD_IMAGE, Key.L, LoadImageRequested())
    contents += menuItem(GuiConstants.MI_DELETE_LAYERS, Key.D, DeleteLayersRequested())
    contents += menuItem(GuiConstants.MI_TOGGLE_INPUTS, Key.I, ToggleToolsRequested())
    contents += menuItem(GuiConstants.MI_TOGGLE_GUIDELINE, Key.G, ToggleGuidelineRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  contents += new Menu(GuiConstants.MENU_TRANSFORM) {
    mnemonic = Key.T
    contents += opRequestMenuItem(GuiConstants.MI_SET_C, Key.T, ProjectConstants.OP_SET)
    contents += opRequestMenuItem(GuiConstants.MI_ADD_C, Key.A, ProjectConstants.OP_ADD)
    contents += opRequestMenuItem(GuiConstants.MI_SUB_C, Key.S, ProjectConstants.OP_SUB)
    contents += opRequestMenuItem(GuiConstants.MI_REV_SUB_C, Key.R, ProjectConstants.OP_REV_SUB)
    contents += opRequestMenuItem(GuiConstants.MI_MUL_C, Key.M, ProjectConstants.OP_MUL)
    contents += opRequestMenuItem(GuiConstants.MI_DIV_C, Key.D, ProjectConstants.OP_DIV)
    contents += opRequestMenuItem(GuiConstants.MI_REV_DIV_C, Key.V, ProjectConstants.OP_REV_DIV)
    contents += opRequestMenuItem(GuiConstants.MI_POW_C, Key.P, ProjectConstants.OP_POW)
    contents += opRequestMenuItem(GuiConstants.MI_MIN_C, Key.N, ProjectConstants.OP_MIN)
    contents += opRequestMenuItem(GuiConstants.MI_MAX_C, Key.X, ProjectConstants.OP_MAX)
    contents += menuItem(GuiConstants.MI_INVERSE, Key.I, InversionRequested())
    contents += menuItem(GuiConstants.MI_GRAYSCALE, Key.G, GrayscaleRequested())
  }

  contents += new Menu(GuiConstants.MENU_HELP) {
    mnemonic = Key.H
    contents += menuItem(GuiConstants.MI_SHORTCUTS, Key.S, ToggleShortcutsRequested())
    contents += menuItem(GuiConstants.MI_VERSION, Key.V, VersionRequested())
    // Note: For whatever reason, contents length here is 0! There is no way to iterate it.
  }

  def reset() {
    Project.instance match {
      case Some(_) =>
        _newMenuItem.enabled = false
        _openMenuItem.enabled = false
        _saveMenuItem.enabled = true
        _saveImageMenuItem.enabled = true
        _closeMenuItem.enabled = true
        for { i <- 1 to 2 } contents(i).enabled = true
      case None =>
        _newMenuItem.enabled = true
        _openMenuItem.enabled = true
        _saveMenuItem.enabled = false
        _saveImageMenuItem.enabled = false
        _closeMenuItem.enabled = false
        for { i <- 1 to 2 } contents(i).enabled = false
    }
  }
  reset()
}

object MyMenuBar {
  private val _instance = new MyMenuBar()
  def instance = _instance
}
