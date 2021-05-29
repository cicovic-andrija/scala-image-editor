package fotoshop.gui

import fotoshop.proj.{Layer, Project}

import scala.swing._
import scala.swing.Swing

class LayerList private extends BoxPanel(Orientation.Vertical) {
  def addLayerPanel(layer: Layer) {
    contents += Swing.VStrut(10)
    contents += new LayerPanel(layer.image)
  }

  def refreshLayers() {
    Project.instance match {
      case None => contents.clear()
      case Some(p) => p.layers foreach addLayerPanel
    }
  }
}

object LayerList {
  private val _instance = new LayerList()
  def instance = _instance
}
