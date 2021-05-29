package fotoshop.proj

import fotoshop.gui.GuiConstants
import fotoshop.util.Extensions._

import java.io.File
import scala.language.postfixOps
import scala.xml.{Elem, XML}

class Project private(private val _file: File, xmlData: xml.NodeSeq) {
  private var _name: String = xmlData \@ "Name" ifEmpty { throw new Exception() } trim
  private var _output = new Output(xmlData \ "Output")
  private var _layers = xmlData \ "Layers" \ "Layer" map { new Layer(_) }
  private var _dirty = false

  def name = _name
  def output = _output
  def layers = _layers
  def dirty = _dirty
  def file = _file

  def toXML: xml.Elem = <Project Name={ name }>
  { output.toXML }
  <Layers>
    { layers map { _.toXML } }
  </Layers>
</Project>

  def save() {
    if (!_dirty) return
    XML.save(file.getPath, toXML, GuiConstants.XML_ENC_UTF_8, xmlDecl = true)
    _dirty = false
  }
}

object Project {
  private var _instance: Option[Project] = None
  def instance = _instance

  def load(file: File) {
    require(file != null)

    try {
      val xmlData = XML.loadFile(file)
      _instance = Some(new Project(file, xmlData \\ "Project"))
    }
    catch {
      case _: Throwable => throw new IllegalArgumentException("Corrupted project file.")
    }
  }

  def close() {
    _instance = None
  }
}
