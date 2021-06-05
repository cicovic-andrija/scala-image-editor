package fotoshop.gui

import fotoshop.proj.{Layer, Project}

import scala.swing._
import scala.swing.Swing

class LayerList private extends BoxPanel(Orientation.Vertical) with DeafToSelf {

  def addLayer(layer: Layer) {
    contents += Swing.VStrut(10)
    contents += new LayerPanel(layer)
    listenTo(contents.last)
  }

  def refreshBorders() {
    for { c <- contents if c.isInstanceOf[LayerPanel] } c.asInstanceOf[LayerPanel].refreshBorder()
  }

  def reload() {
    contents foreach { deafTo(_) }
    contents.clear()
    Project.instance match {
      case Some(project) =>
        project.layers foreach { layer => addLayer(layer) }
      case None =>
    }
  }

  override def repaint() {
    revalidate() // 2h of debugging to find this line
    super.repaint()
  }

  reactions += {
    case e : LayerToggled => publish(e)
  }
}

object LayerList {
  private val _instance = new LayerList()
  def instance = _instance
}
