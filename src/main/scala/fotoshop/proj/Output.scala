package fotoshop.proj

import fotoshop.util.Extensions.StringExtensions

import scala.language.postfixOps

class Output private[proj](private val owner: Project, xmlData: xml.NodeSeq) {
  private val _width: Int = xmlData  \@ "Width" toInt
  private val _height: Int = xmlData \@ "Height" toInt
  private val _imagePath: String = xmlData \@ "ImagePath" ifEmpty { throw new Exception() } trim

  def width = _width
  def height = _height
  def imagePath = _imagePath

  def toXML: xml.Elem = <Output Width={ width toString } Height={ height toString } ImagePath={ imagePath } />
}
