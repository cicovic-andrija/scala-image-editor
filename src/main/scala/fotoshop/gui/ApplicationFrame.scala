package fotoshop.gui

class ApplicationFrame extends scala.swing.MainFrame {
  title = GuiConstants.FRAME_TITLE
  resizable = true
  size = GuiConstants.FRAME_PREF_SIZE // NOTE: Needed for centerOnScreen() to work(?).
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  centerOnScreen()
  menuBar = new CustomMenuBar()
  contents = new MainPanel()
}
