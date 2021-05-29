package fotoshop.proj

import fotoshop.util.Extensions._

import scala.language.postfixOps

class Project private(xmlData: xml.NodeSeq) {
  private var _name: String = xmlData \@ "Name" ifEmpty { throw new Exception() } trim
  private var _output = new Output(xmlData \ "Output")
  private var _layers = xmlData \ "Layers" \ "Layer" map { new Layer(_) }

  def name = _name
  def output = _output
  def layers = _layers
}

object Project {
  private var _instance: Option[Project] = None
  def instance = _instance

  def load(xmlData: xml.Elem) {
    require(xmlData != null)

    try {
      _instance = Some(new Project(xmlData \\ "Project"))
    }
    catch {
      case _: Throwable => throw new IllegalArgumentException("Corrupted project file.")
    }
  }

  def close() {
    _instance = None
  }
}
