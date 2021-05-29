package fotoshop.gui

import scala.language.reflectiveCalls

trait Refreshable {
  this: {
    def repaint(): Unit
  } =>

  def preRefresh(): Unit

  def refresh() {
    preRefresh()
    repaint()
  }
}
