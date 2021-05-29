package fotoshop.gui

import java.awt.Image
import scala.swing.{BorderPanel, Dimension, Graphics2D}

class LayerPanel(private val _image: Image) extends BorderPanel {
  require(_image != null)

  border = GuiComponents.blackBorder
  preferredSize = GuiConstants.LAYER_PANEL_SIZE
  minimumSize = GuiConstants.LAYER_PANEL_SIZE
  maximumSize = GuiConstants.LAYER_PANEL_SIZE

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    g.drawImage(_image.getScaledInstance(GuiConstants.LAYER_PANEL_WIDTH, GuiConstants.LAYER_PANEL_HEIGHT, Image.SCALE_DEFAULT), 0, 0, null)
  }
}
