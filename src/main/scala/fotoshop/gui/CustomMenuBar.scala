package fotoshop.gui

import scala.swing.event.{EditDone, Event, Key}
import scala.swing._

case class ProjectOpened(projectName: String) extends Event

class CustomMenuBar private extends MenuBar {

  // Helper class
  implicit class MenuItemExtensions(mi: MenuItem) {
    def setProperties(key: Key.Value): MenuItem = {
      mi.preferredSize = new Dimension(GuiConstants.MI_WIDTH, mi.peer.getPreferredSize.height)
      mi.mnemonic = key
      mi
    }
  }

  // Project menu
  contents += new Menu(GuiConstants.MENU_PROJECT) {
    mnemonic = Key.P

    contents += new MenuItem(
      Action(GuiConstants.MI_NEW_PROJECT){
      }
    ).setProperties(Key.N)

    contents += new MenuItem(
      Action(GuiConstants.MI_OPEN_PROJECT) {
        val name = GuiComponents.openProject()
        name match {
          case Some(s) => publish(ProjectOpened(s))
          case None =>
        }
      }
    ).setProperties(Key.O)

    contents += new MenuItem(
      Action(GuiConstants.MI_SAVE_PROJECT) {
      }
    ).setProperties(Key.S)

    contents += new MenuItem(
      Action(GuiConstants.MI_CLOSE_PROJECT) {
      }
    ).setProperties(Key.C)

    // Add a separator before exit menu item
    contents += new Separator()

    contents += new MenuItem(
      Action(GuiConstants.MI_EXIT) {
        sys.exit(0) // FIXME: Is there a more elegant way to exit?
      }
    ).setProperties(Key.X)

    contents foreach { listenTo(_) }
    deafTo(this)
    reactions += {
      case e: ProjectOpened => publish(e)
    }
  }

  // Help menu
  contents += new Menu(GuiConstants.MENU_HELP) {
    mnemonic = Key.H

    // Tools menu item
    contents += new MenuItem(
      Action(GuiConstants.MI_TOOLS) {
        GuiComponents.toolsPanel.toggle()
      }
    ).setProperties(Key.T)

    // Shortcuts menu item
    contents += new MenuItem(
      Action(GuiConstants.MI_SHORTCUTS) {
        GuiComponents.shortcutsPanel.toggle()
      }
    ).setProperties(Key.S)

    // Version menu item
    contents += new MenuItem(
      Action(GuiConstants.MI_VERSION) {
        Dialog.showMessage(null, GuiConstants.VER_MESSAGE, GuiConstants.VER_DIAG_TITLE)
      }
    ).setProperties(Key.V)

    contents foreach { listenTo(_) }
  }

  contents foreach { listenTo(_) }
  deafTo(this)
  //reactions += {
  //  case e: ProjectOpened => publish(e)
  //}
}

object CustomMenuBar {
  private val _instance = new CustomMenuBar()
  def instance = _instance
}
