package fotoshop.main

import fotoshop.gui._

import scala.swing.SimpleSwingApplication

object Main extends SimpleSwingApplication {
  def top = ApplicationFrame.instance
  listenTo(top)
  reactions += {
    case _: ExitRequested => quit()
  }
}
