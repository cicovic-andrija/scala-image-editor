package fotoshop.gui

import scala.swing.Publisher

trait DeafToSelf extends Publisher {
  deafTo(this)
}
