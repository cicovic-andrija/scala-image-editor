package fotoshop.proj

import fotoshop.util.Extensions.StringExtensions

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.language.postfixOps

class Layer private[proj] (xmlData: xml.NodeSeq) {
  private val _path: String = xmlData \@ "ImagePath" ifEmpty { throw new Exception() } trim
  private val _image: BufferedImage = ImageIO.read(new File(_path))

  def path = _path
  def image = _image
}
