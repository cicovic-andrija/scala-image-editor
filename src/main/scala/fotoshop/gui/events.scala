package fotoshop.gui

import scala.swing.event.Event

trait CustomEvent       extends Event
trait MenuBarEvent      extends CustomEvent

case class NewProjectRequested()      extends MenuBarEvent
case class OpenRequested()            extends MenuBarEvent
case class SaveRequested()            extends MenuBarEvent
case class CloseRequested()           extends MenuBarEvent
case class ExitRequested()            extends MenuBarEvent
case class ToggleToolsRequested()     extends MenuBarEvent
case class ToggleShortcutsRequested() extends MenuBarEvent
case class VersionRequested()         extends MenuBarEvent
