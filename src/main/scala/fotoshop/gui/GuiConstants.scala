package fotoshop.gui

import fotoshop.proj.ProjectConstants._

import java.awt.Color
import scala.swing.Dimension

object GuiConstants {
  val MI_WIDTH              : Int       = 128
  val SIDEBAR_WIDTH         : Int       = 288
  val SHR_TABLE_HEIGHT      : Int       = 192
  val LAYER_PANEL_WIDTH     : Int       = 256
  val LAYER_PANEL_HEIGHT    : Int       = 144
  val LINE_THICKNESS        : Int       = 2
  val V_GAP_SIZE            : Int       = 3
  val H_GAP_SIZE            : Int       = 5
  val TEXT_FIELD_WIDTH      : Int       = 10
  val APP_VERSION           : String    = "0.1 (c) Andrija Cicovic, 2021."
  val FRAME_TITLE           : String    = "FotoShop"
  val MENU_PROJECT          : String    = "Project"
  val MENU_HELP             : String    = "Help"
  val MI_NEW_PROJECT        : String    = "New Project..."
  val MI_OPEN_PROJECT       : String    = "Open Project..."
  val MI_SAVE_PROJECT       : String    = "Save Project..."
  val MI_CLOSE_PROJECT      : String    = "Close Project"
  val MI_EXIT               : String    = "Exit"
  val MI_TOOLS              : String    = "Toggle Tools"
  val MI_SHORTCUTS          : String    = "Toggle Shortcuts"
  val MI_VERSION            : String    = "Version"
  val TB_TOOLS              : String    = "Tools"
  val TB_LAYERS             : String    = "Layers"
  val TB_SHORTCUTS          : String    = "Shortcuts"
  val NEW_PROJ_TITLE        : String    = "New Project"
  val NEW_PROJ_NAME_LBL     : String    = "Project Name:"
  val NEW_PROJ_DEST_DIR_LBL : String    = "Location:"
  val NEW_PROJ_WIDTH_LBL    : String    = "Width:"
  val NEW_PROJ_HEIGHT_LBL   : String    = "Height:"
  val NEW_PROJ_CHOOSE_BTN   : String    = "Choose..."
  val NEW_PROJ_CREATE_BTN   : String    = "Create"
  val NEW_PROJ_SIZE_FORMAT  : String    = "from %d to %d".format(OUTPUT_MIN_W_H, OUTPUT_MAX_W_H)
  val NEW_PROJ_NAME_FORMAT  : String    = "a-z A-Z _ 0-9"
  val DEST_DIR_DIALOG_TITLE : String    = "Select project location"
  val EXT_XML               : String    = "xml"
  val EXT_JPG               : String    = "jpg"
  val EXT_JPEG              : String    = "jpeg"
  val EXT_PNG               : String    = "png"
  val XML_ENC_UTF_8         : String    = "UTF-8"
  val SB_ERROR_PREFIX       : String    = "ERROR: "
  val SB_INIT_TEXT          : String    = "Initializing...Done."
  val SB_TEXT_READY         : String    = "Ready."
  val SB_TEXT_PROJ_CLOSED   : String    = "Project closed."
  val SB_FMT_NEW_PROJ_FAIL  : String    = "Failed to create project %s. Please try again."
  val SB_FMT_CORRUPTED_PROJ : String    = "Failed to open project from file %s. The file is possibly corrupted."
  val SB_FMT_SAVE_FAILED    : String    = "Failed to save the project to file %s."
  val SB_LOAD_IMG_FAILED    : String    = "Failed to load image."
  val SB_OP_NOT_SUPPORTED   : String    = "Attempted operation is not supported."
  val SB_FMT_SAVE_SUCCEEDED : String    = "Project saved to file %s."
  val OPEN_DIALOG_FILE_DESC : String    = "XML Files"
  val OPEN_DIALOG_TITLE     : String    = "Select project file"
  val LOAD_IMG_DIALOG_TITLE : String    = "Select image file"
  val LOAD_IMG_FILE_DESC    : String    = "JPG/PNG Files"
  val VER_DIALOG_TITLE      : String    = FRAME_TITLE + " " + MI_VERSION + " Information"
  val VER_MESSAGE           : String    = FRAME_TITLE + " " + APP_VERSION
  val FRAME_MIN_SIZE        : Dimension = new Dimension(768, 432)
  val FRAME_PREF_SIZE       : Dimension = new Dimension(1600, 900)
  val LAYER_PANEL_SIZE      : Dimension = new Dimension(LAYER_PANEL_WIDTH, LAYER_PANEL_HEIGHT)
  val COLOR_LIGHT_GRAY      : Color     = Color.LIGHT_GRAY
  val COLOR_BLACK           : Color     = new Color(0, 0, 0)
  val COLOR_WHITE           : Color     = new Color(255, 255, 255)
  val COLOR_RED             : Color     = new Color(255, 0, 0)
  val COLOR_GREEN           : Color     = new Color(0, 255, 0)
}
