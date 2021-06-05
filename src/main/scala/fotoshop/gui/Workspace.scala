package fotoshop.gui

import fotoshop.proj._

import scala.swing._
import java.awt.AlphaComposite
import java.awt.image.BufferedImage

class Workspace(width: Int, height: Int) extends Panel {

  preferredSize = new Dimension(width, height)
  minimumSize = new Dimension(width, height)
  maximumSize = new Dimension(width, height)
  background = GuiConstants.COLOR_WHITE

  def drawLayers(project: Project, g: Graphics2D) {
    val solidComposite = g.getComposite

    def drawOneLayer = (layer: Layer) => {
      if (layer.visible) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.transparency))
        g.drawImage(layer.image, layer.x, layer.y, null)
      }
    }

    project.layers foreach { drawOneLayer(_) }

    g.setComposite(solidComposite)
    if (project.guideline) {
      g.drawRect(0, 0, project.output.width, project.output.height)
    }
  }

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    Project.instance match {
      case Some(project) => drawLayers(project, g)
      case None =>
    }
  }
}

object Workspace {
  val Empty = new Workspace(0, 0)
}
