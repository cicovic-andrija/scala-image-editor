package fotoshop.gui

import fotoshop.proj.{Layer, Project}

import scala.collection.mutable.ListBuffer
import scala.swing._
import scala.swing.Swing

class LayerList private extends BoxPanel(Orientation.Vertical) with DeafToSelf {

  private val layers = ListBuffer[LayerPanel]()

  def addLayerPanel(layer: Layer) {
    layers += new LayerPanel(layer)
    contents += Swing.VStrut(10)
    contents += layers.last
    listenTo(layers.last)
  }

  def reloadLayers() {
    contents foreach { deafTo(_) }
    contents.clear()
    Project.instance match {
      case Some(project) => project.layers foreach { layer => addLayerPanel(layer) }
      case None => // do nothing
    }
  }

  reactions += {
    case e : LayerToggled => publish(e)
  }
}

object LayerList {
  private val _instance = new LayerList()
  def instance = _instance
}
