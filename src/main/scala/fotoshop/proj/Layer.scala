package fotoshop.proj

import fotoshop.util.Extensions.StringExtensions

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.language.postfixOps

class Layer private[proj](private val owner: Project, xmlData: xml.NodeSeq) {

  // serializable fields
  private val _path: String = xmlData \@ "ImagePath" ifEmpty { throw new Exception() } trim
  private var _visible = xmlData \@ "Visible" toBoolean
  private var _transparency = xmlData \@ "Transparency" toFloat
  private var _x = xmlData \@ "X" toInt
  private var _y = xmlData \@ "Y" toInt

  // non-serializable fields
  private val _image: BufferedImage = ImageIO.read(new File(_path))
  private var _selected = false

  if (_image == null) {
    throw new Exception()
  }

  def path = _path
  def image = _image
  def x = _x
  def y = _y

  def selected = _selected
  def selected_=(selected: Boolean): Unit = {
    _selected = selected
  }

  def visible = _visible
  def toggleVisible() {
    _visible = !_visible
    owner.markDirty()
  }

  def moveOnX(step: Int) {
    if (x + image.getWidth + step > owner.output.width) {
      _x = owner.output.width - image.getWidth
    } else if (x + step < 0) {
      _x = 0
    } else {
      _x = x + step
    }
  }

  def moveOnY(step: Int) {
    if (y + image.getHeight + step > owner.output.height) {
      _y = owner.output.height - image.getHeight
    } else if (y + step < 0) {
      _y = 0
    } else {
      _y= y + step
    }
  }

  def transparency = _transparency
  def updateTransparency(delta: Float) {
    _transparency = if (transparency + delta < 0.0f) 0.0f else if (transparency + delta > 1.0f) 1.0f else transparency + delta
    owner.markDirty()
  }

  def toXML: xml.Elem = <Layer ImagePath={path} Visible={visible toString} Transparency={ transparency toString } X={ x toString } Y={ y toString }/>
}
