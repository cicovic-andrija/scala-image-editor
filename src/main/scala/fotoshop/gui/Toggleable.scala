package fotoshop.gui

import scala.language.reflectiveCalls // FIXME: Figure out why this is needed?

trait Toggleable {
  this: {
    def visible: Boolean
    def visible_=(b: Boolean): Unit
  } =>

  def toggle() { visible = !visible }
}
