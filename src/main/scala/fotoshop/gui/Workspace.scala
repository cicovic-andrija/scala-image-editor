package fotoshop.gui

import fotoshop.proj.Project

import scala.swing._

class Workspace(private val project: Project) extends Panel {

  preferredSize = new Dimension(project.output.width, project.output.height)
  minimumSize = new Dimension(project.output.width, project.output.height)
  maximumSize = new Dimension(project.output.width, project.output.height)
  background = GuiConstants.COLOR_WHITE

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    Project.instance match {
      case Some(p) =>
        p.layers foreach { l => if (l.visible) g.drawImage(l.image, 0, 0, null) }
        if (project.guideline) {
          g.drawRect(0, 0, project.output.width, project.output.height)
        }
      case None =>
    }
  }
}

object Workspace {
  val Empty = new Panel() {
    preferredSize = new Dimension(0, 0)
    background = GuiConstants.COLOR_WHITE
  }
}
