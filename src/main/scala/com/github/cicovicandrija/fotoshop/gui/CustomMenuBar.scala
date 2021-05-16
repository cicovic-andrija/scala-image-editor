package com.github.cicovicandrija.fotoshop.gui

import scala.swing.event.Key
import scala.swing.{Action, Dimension, Menu, MenuBar, MenuItem, Separator}

class CustomMenuBar extends MenuBar {
  private val menuItemWidth = 128

  contents += new Menu("Project") {
    mnemonic = Key.P

    contents += new MenuItem("New Project...") {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.N
    }
    contents += new MenuItem("Open Project...") {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.O
    }
    contents += new MenuItem("Save Project...") {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.S
    }
    contents += new MenuItem("Close Project") {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.C
    }
    contents += new Separator()
    contents += new MenuItem(Action("Exit") { sys.exit(0) }) {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.X
    }
  }

  contents += new Menu("About") {
    mnemonic = Key.A

    contents += new MenuItem("Shortcuts") {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.S
    }
    contents += new MenuItem("Version") {
      preferredSize = new Dimension(menuItemWidth, peer.getPreferredSize.height)
      mnemonic = Key.V

    }
  }
}
