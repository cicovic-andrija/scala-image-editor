package fotoshop.gui

import fotoshop.proj.{Project, ProjectConstants}
import GuiComponents.{redBorder, blackBorder}

import javax.swing.border.TitledBorder
import scala.language.postfixOps
import scala.swing._
import scala.swing.event.{ButtonClicked, EditDone}
import scala.util.Try

class InputsPanel extends BoxPanel(Orientation.Horizontal) with Toggleable {

  border = new TitledBorder(GuiConstants.TB_INPUTS)
  visible = true

  contents += new TextField()
  contents += new CheckBox(ProjectConstants.RGB_R)
  contents += new CheckBox(ProjectConstants.RGB_G)
  contents += new CheckBox(ProjectConstants.RGB_B)

  private var rgbFlags = Set[String]()
  private var C = -1

  def updateC(text: String): Int = {
    C = Try { text toInt} getOrElse -1
    C
  }

  def updatedRgbSet(rgbComponent: String)(b: Boolean) = if (b) rgbFlags + rgbComponent else rgbFlags - rgbComponent

  def gatherInput: InputEvent = {
    if (C < 0) InputProvided(valid = false) else MultiInputProvided(Input(C, rgbFlags))
  }

  def reset() {
    Project.instance match {
      case Some(_) =>
        contents foreach { _.enabled = true }
      case None =>
        contents foreach { _.enabled = false }
    }
  }
  reset()

  reactions += {
    case ButtonClicked(source) => rgbFlags = updatedRgbSet(source.text)(source.selected)
    case EditDone(source) => source.border = if (updateC(source.text) < 0) redBorder else blackBorder
  }
  contents foreach { listenTo(_) }
}
