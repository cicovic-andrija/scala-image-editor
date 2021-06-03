package fotoshop.gui

import fotoshop.proj.Layer
import fotoshop.util.Extensions.IntExtensions

import scala.swing._
import java.awt.Image
import scala.swing.event.{Event, Key, MouseClicked}
import scala.swing.event.Key.Modifier

class LayerPanel(private val layer: Layer) extends BorderPanel {
  require(layer != null)

  preferredSize = GuiConstants.LAYER_PANEL_SIZE
  minimumSize = GuiConstants.LAYER_PANEL_SIZE
  maximumSize = GuiConstants.LAYER_PANEL_SIZE

  def refreshBorder() {
    border = if (layer.visible) GuiComponents.thickBlackBorder else GuiComponents.thickRedBorder
  }
  refreshBorder()

  def toggleVisible() {
    layer.toggleVisible()
    refreshBorder()
  }

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    g.drawImage(layer.image.getScaledInstance(GuiConstants.LAYER_PANEL_WIDTH, GuiConstants.LAYER_PANEL_HEIGHT, Image.SCALE_DEFAULT), 0, 0, null)
  }

  reactions += {
    case MouseClicked(_, _, mod, _, _) if mod mask Modifier.Control =>
      toggleVisible()
      publish(LayerToggled())
  }
  listenTo(mouse.clicks)
}
