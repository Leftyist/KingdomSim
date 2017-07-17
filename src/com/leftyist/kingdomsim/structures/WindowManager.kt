package com.leftyist.kingdomsim.structures

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.event.MenuKeyListener
import java.awt.event.ActionListener
import java.nio.file.DirectoryStream
import javax.swing.*

object WindowManager {

      val mainFrame: JFrame = JFrame()
      val saveLocation = "/saves"

      fun loadFrame() {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
            mainFrame.setJMenuBar(makeMenu())

            mainFrame.minimumSize = Dimension(300,300)
            mainFrame.setResizable(false)
            mainFrame.pack()
            mainFrame.setLocationRelativeTo(null)
            mainFrame.setVisible(true)
      }

      private fun makeMenu(): JMenuBar {
            val menuBar: JMenuBar

            //Create the menu bar.
            menuBar = JMenuBar()

            //=====================================================================
            //======================== File Menu ==================================
            //=====================================================================

            val fileMenu = JMenu("File")
            fileMenu.mnemonic = KeyEvent.VK_F
            menuBar.add(fileMenu)

            //submenu
            fileMenu.addSeparator()
            val fileSubmenu = JMenu("New...")
            fileSubmenu.mnemonic = KeyEvent.VK_N

            val newWorldItem = JMenuItem("World")
            newWorldItem.mnemonic = KeyEvent.VK_W
            newWorldItem.addActionListener(ActionListener {
                  newWorld()
            })
            fileSubmenu.add(newWorldItem)

            fileMenu.add(fileSubmenu)

            return menuBar
      }

      private fun newWorld() {

      }

      private fun openFile() {
            val fc = JFileChooser()
            fc.showOpenDialog(mainFrame)
      }
}