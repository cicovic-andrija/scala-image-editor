package fotoshop.main

import fotoshop.gui._

import scala.swing.SimpleSwingApplication

object Main extends SimpleSwingApplication {
  def top = ApplicationFrame.instance
  reactions += {
    case _: ExitRequested => quit()
  }
  listenTo(top)
}
