package fotoshop.proj

import fotoshop.util.Extensions._

import scala.xml._
import scala.collection.mutable._
import scala.language.postfixOps
import java.io.File

class Project private(private val _filePath: String, xmlData: xml.NodeSeq) {
  private val _name: String = xmlData \@ "Name" ifEmpty { throw new Exception() } trim
  private val _output = new Output(owner = this, xmlData \ "Output")
  private var _layers = ListBuffer(xmlData \ "Layers" \ "Layer" map { new Layer(owner = this, _) }: _*)
  private var _dirty = false
  private var _guideline = false

  def name = _name
  def output = _output
  def layers = _layers
  def dirty = _dirty
  def guideline = _guideline
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

  def toggleGuideline() {
    _guideline = !_guideline
  }

  def save() {
    if (!dirty) return
    XML.save(filePath, toXML, ProjectConstants.XML_ENC_UTF_8, xmlDecl = true)
    _dirty = false
  }

  def loadImage(imgPath: String): Layer = {
    _layers += new Layer(owner = this, Layer.newXML(imgPath))
    markDirty()
    _layers.last
  }

  def selectedLayers = _layers count { _.selected }

  def deleteSelectedLayers() {
    if (selectedLayers > 0) {
      _layers = _layers filter {
        !_.selected
      }
      markDirty()
    }
  }

  def unselectAll() {
    _layers foreach { _.selected = false }
  }

  def moveLayer(newIdx: Int => Int) {
    val idx = _layers indexWhere { _.selected }
    _layers.insert(newIdx(idx), _layers.remove(idx))
    markDirty()
  }

  def moveLayerUp(): Boolean = {
    // Move only if one layer is selected, and not the first one
    if ((!(selectedLayers == 1)) || _layers.head.selected) {
      return selectedLayers == 1
    }
    moveLayer(idx => idx - 1)
    true
  }

  def moveLayerDown(): Boolean = {
    // Move only if one layer is selected, and not the last one
    if ((!(selectedLayers == 1)) || _layers.last.selected) {
      return selectedLayers == 1
    }
    moveLayer(idx => idx + 1)
    true
  }

  def moveImagesOnX(step: Int) {
    _layers foreach { layer => if (layer.selected) layer.moveOnX(step) }
    markDirty()
  }

  def moveImagesOnY(step: Int) {
    _layers foreach { layer => if (layer.selected) layer.moveOnY(step) }
    markDirty()
  }

  def updateTransparency(delta: Float) {
    _layers foreach { layer => if (layer.selected) layer.updateTransparency(delta) }
    markDirty()
  }
}

object Project {
  private var _instance: Option[Project] = None
  def instance = _instance

  def outputImageFile(params: ProjectParams) = new File(params.location, params.name + ProjectConstants.EXT_JPG)

  def template(params: ProjectParams): xml.Elem = <Project Name={ params.name }>
  <Output Width={ params.outputWidth } Height={ params.outputHeight } ImagePath={ outputImageFile(params).getPath }/>
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
    XML.save(file.getPath, template(params), ProjectConstants.XML_ENC_UTF_8, xmlDecl = true)

  def close() {
    _instance = None
  }
}
