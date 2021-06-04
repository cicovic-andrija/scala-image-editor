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

  // non-serializable fields
  private val _image: BufferedImage = ImageIO.read(new File(_path))
  private var _selected = false

  def path = _path
  def image = _image

  def selected = _selected
  def toggleSelected() {
    _selected = !_selected
  }

  def visible = _visible
  def toggleVisible() {
    _visible = !_visible
    owner.markDirty()
  }

  def toXML: xml.Elem = <Layer ImagePath={path} Visible={visible toString}/>
}
