package fotoshop.gui

import scala.language.reflectiveCalls

trait Toggleable {
  this: {
    def visible: Boolean
    def visible_=(b: Boolean): Unit
  } =>

  def toggle() { visible = !visible }
}
