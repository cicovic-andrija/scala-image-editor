package fotoshop.gui

import javax.swing.border.LineBorder
import scala.annotation.tailrec
import scala.swing.ScrollPane.BarPolicy
import scala.swing.{ScrollPane, Table}

object ShortcutsTable {
  private val shortcutsTableHeader = Seq("Shortcut", "Description")

  // FIXME: Is there a way to create Array[Array[String]] and use that?
  private val shortcutsTableData: Array[Array[Any]] = Array(
    Array("Alt+P", "Open Project menu"),
    Array("Alt+P+N", "Create new project"),
    Array("Alt+P+O", "Open a project"),
    Array("Alt+P+S", "Save current project"),
    Array("Alt+P+C", "Close current project"),
    Array("Alt+P+X", "Exit"),
    Array("Alt+H", "Open About menu"),
    Array("Alt+H+T", "Display/hide tools"),
    Array("Alt+H+S", "Display/hide shortcuts"),
    Array("Alt+H+V", "Display version info"),
  )

  private val _instance = new ScrollPane() {
    contents = new Table(shortcutsTableData, shortcutsTableHeader) {
      border = new LineBorder(GuiConstants.TABLE_BORDER_C)
      enabled = false
      autoCreateRowSorter = true
    }
    verticalScrollBarPolicy = BarPolicy.AsNeeded
    horizontalScrollBarPolicy = BarPolicy.AsNeeded
  }

  def instance: ScrollPane = _instance
}
