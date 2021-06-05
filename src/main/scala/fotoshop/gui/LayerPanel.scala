package fotoshop.gui

import fotoshop.proj.Layer
import fotoshop.util.Extensions.IntExtensions

import scala.swing._
import java.awt.Image
import scala.swing.event._

class LayerPanel(private val layer: Layer) extends BorderPanel {

  preferredSize = GuiConstants.LAYER_PANEL_SIZE
  minimumSize = GuiConstants.LAYER_PANEL_SIZE
  maximumSize = GuiConstants.LAYER_PANEL_SIZE

  def refreshBorder() {
    border = if (layer.selected) GuiComponents.thickBlueBorder else
               if (layer.visible) GuiComponents.thickBlackBorder else GuiComponents.thickRedBorder
  }
  refreshBorder()

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    g.drawImage(layer.image.getScaledInstance(GuiConstants.LAYER_PANEL_WIDTH, GuiConstants.LAYER_PANEL_HEIGHT, Image.SCALE_DEFAULT), 0, 0, null)
  }

  reactions += {
    case MouseClicked(_, _, mod, _, _) if mod mask Key.Modifier.Control =>
      layer.toggleVisible()
      refreshBorder()
      publish(LayerToggled())
    case MouseClicked(_, _, mod, _, _) if mod == 0 =>
      layer.selected = !layer.selected
      refreshBorder()
  }
  listenTo(mouse.clicks)
}
