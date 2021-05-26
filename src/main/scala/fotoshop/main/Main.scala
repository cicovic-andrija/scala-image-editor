package fotoshop.main

import fotoshop.gui._

object Main extends scala.swing.SimpleSwingApplication {
  def top = ApplicationFrame.instance
  listenTo(top)
  reactions += {
    case _: ExitRequested => quit()
  }
}
