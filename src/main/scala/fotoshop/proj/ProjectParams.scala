package fotoshop.proj

// FIXME: Does it have to be Event?
import scala.swing.event.Event

case class ProjectParams(name: String, location: String, outputWidth: String, outputHeight: String) extends Event
