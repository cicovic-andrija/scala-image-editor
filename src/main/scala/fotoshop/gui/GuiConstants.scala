package fotoshop.gui

import java.awt.Color
import scala.swing.Dimension

object GuiConstants {
  val APP_VERSION      : String    = "0.1 (c) Andrija Cicovic, 2021."
  val FRAME_TITLE      : String    = "FotoShop"
  val MENU_PROJECT     : String    = "Project"
  val MENU_HELP        : String    = "Help"
  val MI_NEW_PROJECT   : String    = "New Project..."
  val MI_OPEN_PROJECT  : String    = "Open Project..."
  val MI_SAVE_PROJECT  : String    = "Save Project..."
  val MI_CLOSE_PROJECT : String    = "Close Project"
  val MI_EXIT          : String    = "Exit"
  val MI_SHORTCUTS     : String    = "Toggle Shortcuts"
  val MI_TOOLS         : String    = "Toggle Tools"
  val MI_VERSION       : String    = "Version"
  val TB_TOOLS         : String    = "Tools"
  val TB_LAYERS        : String    = "Layers"
  val TB_SHORTCUTS     : String    = "Shortcuts"
  val EXT_XML          : String    = "xml"
  val SB_INIT_TEXT     : String    = "Initializing...Done."
  val OPEN_DIAG_FILE_DESC : String = "XML Files"
  val OPEN_DIAG_TITLE  : String    = "Select project file"
  val SHCUT_DIAG_TITLE : String    = MI_SHORTCUTS
  val VER_DIAG_TITLE   : String    = FRAME_TITLE + " " + MI_VERSION + " Information"
  val VER_MESSAGE      : String    = FRAME_TITLE + " " + APP_VERSION
  val MI_WIDTH         : Int       = 128
  val SHR_TABLE_HEIGHT : Int       = 192
  val DEFAULT_BORDER_C : Color     = Color.LIGHT_GRAY
  val TABLE_BORDER_C   : Color     = Color.BLACK
  val FRAME_MIN_SIZE   : Dimension = new Dimension(768, 432)
  val FRAME_PREF_SIZE  : Dimension = new Dimension(1600, 900)
  val DIAG_MIN_SIZE    : Dimension = new Dimension(192, 108)
  val DIAG_PREF_SIZE   : Dimension = DIAG_MIN_SIZE
}
