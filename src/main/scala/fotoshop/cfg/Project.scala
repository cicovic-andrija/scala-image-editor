package fotoshop.cfg

import fotoshop.ext.Extensions._

import scala.language.postfixOps // FIXME: Figure out why this is needed.
import scala.xml.XML

class Project private (xmlData: xml.Elem) {

  class Output private[Project] (xmlData: xml.Elem) {
    private val _width: Int = xmlData \\ "Output" \@ "Width" toInt
    private val _height: Int = xmlData \\ "Output" \@ "Height" toInt

    def width = _width
    def height = _height
  }

  private val _name: String = xmlData \\ "Project" \@ "Name" ifEmpty { throw new Exception } trim
  private val _output = new Output(xmlData)

  def name = _name
  def output = _output
}

object Project {
  private var _instance: Option[Project] = None

  def instance = _instance.get

  def load(xmlData: xml.Elem) {
    try {
      _instance = Some(new Project(xmlData))
    }
    catch {
      case _: Throwable => throw new IllegalArgumentException("Corrupted project file.")
    }
  }

  def close() {
    _instance = None
  }
}
