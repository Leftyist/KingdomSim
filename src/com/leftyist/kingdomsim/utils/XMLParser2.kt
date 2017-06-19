package com.leftyist.kingdomsim.utils

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun openFile(fileName: String): Document? {
      try {
            val file = File(fileName)
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            documentBuilderFactory.isIgnoringElementContentWhitespace = true
            val documentBuilder = documentBuilderFactory.newDocumentBuilder()
            return documentBuilder.parse(file)
      } catch (e: Exception) {
            return null
      }
}

fun findChildByName(nodes: NodeList?, childName: String): NodeList? {
      if (nodes == null)
            return null

      for (i in 0..nodes.length - 1) {
            if (nodes.item(i).nodeName == childName)
                  return nodes.item(i).childNodes
      }
      return null
}

fun getNodeSubValues(nodes: NodeList): ArrayList<Pair<String, String>> {
      val list = ArrayList<Pair<String, String>>()

      for (i in 0..nodes.length - 1) {
            val node = nodes.item(i)

            if (node.nodeName == "#text")
                  continue
            else
                  list.add(Pair(node.nodeName, node.textContent))
      }
      return list
}