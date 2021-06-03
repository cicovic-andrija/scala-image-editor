package fotoshop.proj

import fotoshop.util.Extensions.StringExtensions

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.language.postfixOps

class Layer private[proj](private val owner: Project, xmlData: xml.NodeSeq) {
  private val _path: String = xmlData \@ "ImagePath" ifEmpty { throw new Exception() } trim
  private var _visible = xmlData \@ "Visible" toBoolean
  private val _image: BufferedImage = ImageIO.read(new File(_path))

  def path = _path

  def visible = _visible
  def toggleVisible() {
    _visible = !_visible
    owner.markDirty()
  }

  def image = _image

  def toXML: xml.Elem = <Layer ImagePath={path} Visible={visible toString}/>
}
