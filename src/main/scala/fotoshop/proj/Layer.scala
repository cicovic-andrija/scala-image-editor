package fotoshop.proj

import fotoshop.util.Extensions.StringExtensions

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.language.postfixOps

class Layer private[proj] (xmlData: xml.NodeSeq) {
  private val _path: String = xmlData \@ "ImagePath" ifEmpty { throw new Exception() } trim
  private val _visible = xmlData \@ "Visible" toBoolean
  private val _image: BufferedImage = ImageIO.read(new File(_path))

  def path = _path
  def visible = _visible
  def image = _image

  def toXML: xml.Elem = <Layer ImagePath={path} Visible={visible toString}/>
}
