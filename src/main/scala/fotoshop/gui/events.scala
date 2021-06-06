package fotoshop.gui

import fotoshop.proj.ProjectConstants._
import fotoshop.proj.ProjectParams

import scala.swing.event.Event

case class Input(C: Int, rgbFlags: Set[String])
object Input {
  def Empty = Input(0, Set[String](RGB_R, RGB_G, RGB_B))
}

trait InputEvent        extends Event
trait MenuBarEvent      extends InputEvent

case class NewProjectRequested()                                         extends MenuBarEvent
case class OpenRequested()                                               extends MenuBarEvent
case class SaveRequested()                                               extends MenuBarEvent
case class CloseRequested()                                              extends MenuBarEvent
case class ExitRequested()                                               extends MenuBarEvent
case class ToggleToolsRequested()                                        extends MenuBarEvent
case class ToggleShortcutsRequested()                                    extends MenuBarEvent
case class VersionRequested()                                            extends MenuBarEvent
case class LoadImageRequested()                                          extends MenuBarEvent
case class SaveImageRequested()                                          extends MenuBarEvent
case class DeleteLayersRequested()                                       extends MenuBarEvent
case class ToggleGuidelineRequested()                                    extends MenuBarEvent
case class OperationRequested(op: String, inputEvent: InputEvent)        extends MenuBarEvent
case class InversionRequested()                                          extends MenuBarEvent
case class GrayscaleRequested()                                          extends MenuBarEvent

case class InputProvided(valid: Boolean)                                           extends InputEvent
case class MultiInputProvided(input: Input)                                        extends InputEvent
case class FolderChooserRequested()                                                extends InputEvent
case class CreateProjectRequested(name: String, loc: String, w: String, h: String) extends InputEvent
case class ProjectParamsProvided(params: ProjectParams)                            extends InputEvent
case class LayerToggled()                                                          extends InputEvent
