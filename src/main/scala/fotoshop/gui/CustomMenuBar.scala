package fotoshop.gui

import javax.swing.JMenuItem
import scala.swing.event.Key
import scala.swing._

class CustomMenuBar extends MenuBar {
  // Util. function to determine the preferred size of a menu itme
  private def menuItemDimension(peer: JMenuItem) =
    new Dimension(GuiConstants.MI_WIDTH, peer.getPreferredSize.height)

  // Project menu
  contents += new Menu(GuiConstants.MENU_PROJECT) {
    mnemonic = Key.P

    contents += new MenuItem(GuiConstants.MI_NEW_PROJECT) {
      mnemonic = Key.N
      preferredSize = menuItemDimension(peer)
    }

    contents += new MenuItem(GuiConstants.MI_OPEN_PROJECT) {
      mnemonic = Key.O
      preferredSize = menuItemDimension(peer)
    }

    contents += new MenuItem(GuiConstants.MI_SAVE_PROJECT) {
      mnemonic = Key.S
      preferredSize = menuItemDimension(peer)
    }

    contents += new MenuItem(GuiConstants.MI_CLOSE_PROJECT) {
      mnemonic = Key.C
      preferredSize = menuItemDimension(peer)
    }

    // Add a separator before exit menu item
    contents += new Separator()

    contents += new MenuItem(Action(GuiConstants.MI_EXIT) { sys.exit(0) }) {
      mnemonic = Key.X
      preferredSize = menuItemDimension(peer)
    }
  }

  // Help menu
  contents += new Menu(GuiConstants.MENU_HELP) {
    mnemonic = Key.H

    // Tools menu item
    contents += new MenuItem(
      Action(GuiConstants.MI_TOOLS){
        GuiComponents.ToolsPanel.toggle()
      }){
      mnemonic = Key.T
      preferredSize = menuItemDimension(peer)
    }

    // Shortcuts menu item
    contents += new MenuItem(
      Action(GuiConstants.MI_SHORTCUTS) {
        GuiComponents.ShortcutsPanel.toggle()
      }){
      mnemonic = Key.S
      preferredSize = menuItemDimension(peer)
    }

    // Version menu item
    contents += new MenuItem(
      Action(GuiConstants.MI_VERSION) {
        Dialog.showMessage(null, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIAG_TITLE)
      }){
      mnemonic = Key.V
      preferredSize = menuItemDimension(peer)
    }
  }
}
