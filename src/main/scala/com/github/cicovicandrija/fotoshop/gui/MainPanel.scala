package com.github.cicovicandrija.fotoshop.gui

import java.awt.{AlphaComposite, Image}
import swing._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.border.TitledBorder
import scala.swing.BorderPanel
import java.io.File
import scala.swing.BorderPanel.Position._

class ImagePanel extends Panel {
  val imagePath = "img/nyc.jpg"
  var buffImg: Image = null
  var buffImg2: Image = null

  buffImg = ImageIO.read(new File(imagePath))
  buffImg2 = ImageIO.read(new File("img/bali.jpg"))

  override def paintComponent(g: Graphics2D): Unit = {
    g.setComposite(AlphaComposite getInstance (AlphaComposite.SRC_OVER, 0.5f))

    if (buffImg != null) {
      g.drawImage(buffImg, 0, 0, null)
    }

    if (buffImg2 != null) {
      g.drawImage(buffImg2, 0, 0, buffImg2.getWidth(null), buffImg2.getHeight(null), null)
    }
  }

  def setTransparency = {
    val transp = 0
    val mask = 0x00FFFFFF
    val ashift = 24
    val w = buffImg2.getWidth(null)
    val h = buffImg2.getHeight(null)

    for (i <- 0 until h; j <- 0 until w) {
      val img = buffImg2.asInstanceOf[BufferedImage]
      try {
        img.setRGB(j, i, img.getRGB(j, i) & mask)
      } catch {
        case _: Throwable => println(i)
      }
      //buffImg2 = img
    }
  }

  def loadImage(path: String) = {
    buffImg = ImageIO.read(new File(path))
  }

  def scaleImage(w: Int, h: Int) {
    buffImg = buffImg.getScaledInstance(w, h, Image.SCALE_DEFAULT)
  }
}

class MainPanel extends BorderPanel {
  border = new TitledBorder("Workspace")
  var imgPanel = new ImagePanel()
  var btn = new Button(Action("Load") {
    imgPanel.setTransparency
  })
  imgPanel.scaleImage(1080, 720)
  layout(imgPanel) = Center
  layout(btn) = North
}
