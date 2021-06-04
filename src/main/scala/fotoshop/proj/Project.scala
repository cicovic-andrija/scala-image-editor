package fotoshop.proj

import fotoshop.util.Extensions._

import scala.xml._
import scala.util._
import scala.collection.mutable._
import scala.language.postfixOps
import java.io.File

class Project private(private val _filePath: String, xmlData: xml.NodeSeq) {
  private val _name: String = xmlData \@ "Name" ifEmpty {
    throw new Exception()
  } trim
  private val _output = new Output(owner = this, xmlData \ "Output")
  private var _layers = ListBuffer(xmlData \ "Layers" \ "Layer" map {
    new Layer(owner = this, _)
  }: _*)
  private var _dirty = false

  def name = _name
  def output = _output
  def layers = _layers
  def dirty = _dirty
  def filePath = _filePath

  def toXML: xml.Elem = <Project Name={name}>
    {output.toXML}<Layers>
      {layers map {
        _.toXML
      }}
    </Layers>
  </Project>

  def markDirty() {
    _dirty = true
  }

  def save() {
    if (!dirty) return
    XML.save(filePath, toXML, ProjectConstants.XML_ENC_UTF_8, xmlDecl = true)
    _dirty = false
  }

  def loadImage(imgPath: String): Layer = {
    _layers += new Layer(owner = this, <Layer ImagePath={imgPath} Visible="true"/>)
    markDirty()
    _layers.last
  }

  def deleteSelectedLayers() {
    _layers = _layers filter {
      !_.selected
    }
    markDirty()
  }

  def selectedLayers = _layers count { _.selected }

  def moveLayerUp(): Boolean = {
    // Move only if one layer is selected
    if (!(selectedLayers == 1)) {
      return false
    }

    // Nothing to do
    if (_layers.head.selected) {
      return true
    }

    val idx = _layers indexWhere { _.selected }
    _layers.insert(idx - 1, _layers.remove(idx))
    true
  }

  def moveLayerDown(): Boolean = {
    // Move only if one layer is selected
    if (!(selectedLayers == 1)) {
      return false
    }

    // Nothing to do
    if (_layers.last.selected) {
      return true
    }

    val idx = _layers indexWhere { _.selected }
    _layers.insert(idx + 1, _layers.remove(idx))
    true
  }
}

object Project {
  private var _instance: Option[Project] = None
  def instance = _instance

  def template(params: ProjectParams): xml.Elem = <Project Name={ params.name }>
  <Output Width={ params.outputWidth } Height={ params.outputHeight }/>
  <Layers>
  </Layers>
</Project>

  def load(file: File) {
    try {
      val xmlData = XML.loadFile(file)
      _instance = Some(new Project(file.getPath, xmlData \\ "Project"))
    }
    catch {
      case _: Throwable => throw new IllegalArgumentException("Corrupted project file.")
    }
  }

  def saveNew(params: ProjectParams, file: File) =
    Try(XML.save(file.getPath, template(params), ProjectConstants.XML_ENC_UTF_8, xmlDecl = true))

  def close() {
    _instance = None
  }
}
