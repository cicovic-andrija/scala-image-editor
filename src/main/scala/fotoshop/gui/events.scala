package fotoshop.gui

import fotoshop.proj.ProjectParams

import scala.swing.event.Event

trait CustomEvent       extends Event
trait MenuBarEvent      extends CustomEvent
trait InputEvent        extends CustomEvent

case class NewProjectRequested()      extends MenuBarEvent
case class OpenRequested()            extends MenuBarEvent
case class SaveRequested()            extends MenuBarEvent
case class CloseRequested()           extends MenuBarEvent
case class ExitRequested()            extends MenuBarEvent
case class ToggleToolsRequested()     extends MenuBarEvent
case class ToggleShortcutsRequested() extends MenuBarEvent
case class VersionRequested()         extends MenuBarEvent

case class InputProvided(valid: Boolean)                                           extends InputEvent
case class FolderChooserRequested()                                                extends InputEvent
case class CreateProjectRequested(name: String, loc: String, w: String, h: String) extends InputEvent
case class ProjectParamsProvided(params: ProjectParams)                            extends InputEvent

case class LayerToggled() extends InputEvent
