package fotoshop.gui

import java.awt.Color
import scala.swing.Dimension

object GuiConstants {
  val APP_VERSION           : String    = "0.1 (c) Andrija Cicovic, 2021."
  val FRAME_TITLE           : String    = "FotoShop"
  val MENU_PROJECT          : String    = "Project"
  val MENU_HELP             : String    = "Help"
  val MI_NEW_PROJECT        : String    = "New Project..."
  val MI_OPEN_PROJECT       : String    = "Open Project..."
  val MI_SAVE_PROJECT       : String    = "Save Project..."
  val MI_CLOSE_PROJECT      : String    = "Close Project"
  val MI_EXIT               : String    = "Exit"
  val MI_SHORTCUTS          : String    = "Toggle Shortcuts"
  val MI_TOOLS              : String    = "Toggle Tools"
  val MI_VERSION            : String    = "Version"
  val TB_TOOLS              : String    = "Tools"
  val TB_LAYERS             : String    = "Layers"
  val TB_SHORTCUTS          : String    = "Shortcuts"
  val EXT_XML               : String    = "xml"
  val SB_ERROR_PREFIX       : String    = "ERROR: "
  val SB_INIT_TEXT          : String    = "Initializing...Done."
  val SB_TEXT_READY         : String    = "Ready."
  val SB_TEXT_PROJ_CLOSED   : String    = "Project closed."
  val SB_FMT_CORRUPTED_PROJ : String    = "Failed to open project from file %s. The file is possibly corrupted."
  val OPEN_DIAG_FILE_DESC   : String    = "XML Files"
  val OPEN_DIAG_TITLE       : String    = "Select project file"
  val SHCUT_DIAG_TITLE      : String    = MI_SHORTCUTS
  val VER_DIAG_TITLE        : String    = FRAME_TITLE + " " + MI_VERSION + " Information"
  val VER_MESSAGE           : String    = FRAME_TITLE + " " + APP_VERSION
  val MI_WIDTH              : Int       = 128
  val SIDEBAR_WIDTH         : Int       = 288
  val SHR_TABLE_HEIGHT      : Int       = 192
  val LAYER_PANEL_WIDTH     : Int       = 256
  val LAYER_PANEL_HEIGHT    : Int       = 144
  val LINE_THICKNESS        : Int       = 3
  val COLOR_LIGHT_GRAY      : Color     = Color.LIGHT_GRAY
  val COLOR_BLACK           : Color     = new Color(0, 0, 0)
  val COLOR_WHITE           : Color     = new Color(255, 255, 255)
  val FRAME_MIN_SIZE        : Dimension = new Dimension(768, 432)
  val FRAME_PREF_SIZE       : Dimension = new Dimension(1600, 900)
  val DIAG_MIN_SIZE         : Dimension = new Dimension(192, 108)
  val LAYER_PANEL_SIZE      : Dimension = new Dimension(LAYER_PANEL_WIDTH, LAYER_PANEL_HEIGHT)
  val DIAG_PREF_SIZE        : Dimension = DIAG_MIN_SIZE
}
