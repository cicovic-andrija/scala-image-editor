package fotoshop.proj

import scala.swing.event.Event

case class ProjectParams(name: String, location: String, outputWidth: String, outputHeight: String) extends Event
