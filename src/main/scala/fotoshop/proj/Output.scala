package fotoshop.proj

import scala.language.postfixOps

class Output private[proj] (xmlData: xml.NodeSeq) {
  private val _width: Int = xmlData  \@ "Width" toInt
  private val _height: Int = xmlData \@ "Height" toInt

  def width = _width
  def height = _height

  def toXML: xml.Elem = <Output Width={ width toString } Height={ height toString } />
}
