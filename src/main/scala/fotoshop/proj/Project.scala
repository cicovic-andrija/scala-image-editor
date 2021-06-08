package fotoshop.proj

import fotoshop.util.Extensions._
import fotoshop.proj.ProjectConstants._
import fotoshop.util.Algorithms

import scala.xml._
import scala.collection.mutable._
import scala.language.postfixOps
import java.io.File
import scala.collection._

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
  {output.toXML}
  <Layers>
    { layers map { _.toXML} }
  </Layers>
</Project>

  private def playBackOperations() {
    _layers foreach { layer =>
      layer.selected = true
      layer.operations foreach { op =>
        operationHandler(op.name, op.argument, op.rgbFlags.split(RGB_FLAG_SEPARATOR).toSet, playBackMode = true)
      }
      layer.selected = false
    }
  }
  playBackOperations()

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

  def forSelectedLayers(f: Layer => Unit) {
    _layers foreach { l => if (l.selected) f(l) }
  }

  def operationHandler(op: String, C: Int, rgbFlags: immutable.Set[String], playBackMode: Boolean) {

    def recordOperation(layer: Layer) {
      if (!playBackMode) {
        layer.operations += Operation(op, C, rgbFlags.mkString(RGB_FLAG_SEPARATOR))
        markDirty()
      }
    }

    def executeOperation = (operation: Int => Int) => forSelectedLayers { layer => {
        rgbFlags foreach { color => layer.forEachPixelForColor(color)(operation) }
        recordOperation(layer)
      }
    }

    def executeOperationOnWholePixel = (operation: Int => Int) => forSelectedLayers { layer => {
        layer.forEachPixel(operation)
        recordOperation(layer)
      }
    }

    def applyFilter = (operation: immutable.IndexedSeq[Int] => Int) => forSelectedLayers { layer => {
        rgbFlags foreach { color => layer.applyFilterToNearbyPixelsForColor(C)(color)(operation) }
        recordOperation(layer)
      }
    }

    op match {
      case OP_SET => executeOperation(_ => C)
      case OP_ADD => executeOperation(_ + C)
      case OP_SUB => executeOperation(_ - C)
      case OP_REV_SUB => executeOperation(C - _)
      case OP_MUL => executeOperation(_ * C)
      case OP_DIV => executeOperation(value => if (C == 0) 0 else value / C)
      case OP_REV_DIV => executeOperation(value => if (value == 0) 0 else C / value)
      case OP_POW if C > 0 => executeOperation(value => math.pow(value, C).intValue)
      case OP_LOG if C > 0 => executeOperation(_ => math.log(C).intValue)
      case OP_MIN => executeOperation(_.min(C))
      case OP_MAX => executeOperation(_.max(C))
      case OP_GRAYSCALE => executeOperationOnWholePixel(Algorithms.grayscale)
      case OP_FILTER_MEDIAN if C > 0 => applyFilter(Algorithms.median)
      case OP_FILTER_AVG if C > 0 => applyFilter(Algorithms.average)
      case _ =>
    }
  }

  def operationHandler(op: String, C: Int, rgbFlags: immutable.Set[String]) {
    operationHandler(op, C, rgbFlags, playBackMode = false)
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
