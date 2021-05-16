package com.github.cicovicandrija.fotoshop.gui

import scala.swing.{Dimension, MainFrame}

class FotoShopApp extends MainFrame {
  private val minDim = new Dimension(768, 432)
  private val dim = new Dimension(1600, 900)

  title = "FotoShop"
  size = dim
  preferredSize = dim
  minimumSize = minDim
  resizable = true
  centerOnScreen()

  menuBar = new CustomMenuBar()
  contents = new MainPanel()
}
