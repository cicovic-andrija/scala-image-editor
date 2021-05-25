package fotoshop.gui

class ApplicationFrame private extends scala.swing.MainFrame {
  title = GuiConstants.FRAME_TITLE
  resizable = true
  // FIXME: Needed for centerOnScreen() to work(?).
  size = GuiConstants.FRAME_PREF_SIZE
  preferredSize = GuiConstants.FRAME_PREF_SIZE
  minimumSize = GuiConstants.FRAME_MIN_SIZE
  centerOnScreen()
  menuBar = CustomMenuBar.instance
  contents = new MainPanel()

  listenTo(CustomMenuBar.instance)
  reactions += {
    case e: ProjectOpened => { println("Got here"); title = GuiConstants.FRAME_TITLE + " - " + e.projectName }
  }
}

object ApplicationFrame {
  private val _instance = new ApplicationFrame()

  def instance = _instance

  def appendProjectNameToTitle(name: String) {
    _instance.title = GuiConstants.FRAME_TITLE + " - " + name
  }

  def setDefaultTitle() {
    _instance.title = GuiConstants.FRAME_TITLE
  }
}
