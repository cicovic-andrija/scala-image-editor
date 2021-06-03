package fotoshop.gui

import fotoshop.proj.Project

import scala.swing._

class Workspace(width: Int, height: Int) extends Panel {
  val dim = new Dimension(width, height)
  preferredSize = dim
  minimumSize = dim
  maximumSize = dim
  background = GuiConstants.COLOR_WHITE

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    Project.instance match {
      case Some(p) => p.layers foreach { l => if (l.visible) g.drawImage(l.image, 0, 0, null) }
      case None =>
    }
  }
}
