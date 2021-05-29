package fotoshop.gui

import javax.swing.border.LineBorder
import scala.swing._
import scala.swing.ScrollPane.BarPolicy

object ShortcutsTable {
  private val shortcutsTableHeader: Seq[String] = Seq("Shortcut", "Description")
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

  private val _instance = new Table(shortcutsTableData, shortcutsTableHeader) {
    border = new LineBorder(GuiConstants.COLOR_BLACK)
    enabled = false
    autoCreateRowSorter = true
  }

  def instance: Table = _instance
}
