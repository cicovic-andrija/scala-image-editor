package fotoshop.proj

import scala.language.postfixOps

case class Operation private[proj](name: String, argument: Int, rgbFlags: String) {
  def toXML: xml.Elem = <Operation Name={ name } Argument={ argument toString } RGBFlags={ rgbFlags } />
}

object Operation {
  def fromXML(xmlData: xml.NodeSeq) =
    Operation(xmlData \@ "Name", xmlData \@ "Argument" toInt, xmlData \@ "RGBFlags")
}
