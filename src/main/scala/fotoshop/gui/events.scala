package fotoshop.gui

import scala.swing.event.Event

trait CustomEvent       extends Event
trait MenuBarEvent      extends CustomEvent

case class ExitRequested()       extends MenuBarEvent
case class NewProjectRequested() extends MenuBarEvent
case class OpenProject()         extends MenuBarEvent
case class SaveProject()         extends MenuBarEvent
case class CloseProject()        extends MenuBarEvent
case class ToggleTools()         extends MenuBarEvent
case class ToggleShortcuts()     extends MenuBarEvent
case class ShowVersion()         extends MenuBarEvent
