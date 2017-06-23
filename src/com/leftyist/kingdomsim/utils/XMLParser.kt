package com.leftyist.kingdomsim.utils

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun openFile(fileName: String): Document {
      try {
            val file = File(fileName)
            val documentBuilderFactory = DocumentBuilderFactory.newInstance()
            documentBuilderFactory.isIgnoringElementContentWhitespace = true
            val documentBuilder = documentBuilderFactory.newDocumentBuilder()
            return documentBuilder.parse(file)
      } catch (e: Exception) {
            throw IllegalArgumentException("File not found")
      }
}

fun NodeList.findChild(childName: String): Node? {
      if (this == null)
            return null

      for (i in 0..this.length - 1) {
            if (this.item(i).nodeName == childName)
                  return this.item(i)
      }
      return null
}

fun Node.findChild(childName: String): Node? {
      return this.childNodes.findChild(childName)
}

fun getNodeSubValues(node: Node): ArrayList<Pair<String, String>> {
      val list = ArrayList<Pair<String, String>>()

      val nodes = node.childNodes

      for (i in 0..nodes.length - 1) {
            val subnode = nodes.item(i)

            if (subnode.nodeName == "#text")
                  continue
            else
                  list.add(Pair(subnode.nodeName, getValue(subnode)))

      }
      return list
}

fun findValueInNode(node: Node, value: String): String {
      val nodes = node.childNodes
      for(i in 0..nodes.length) {
            val subnode = nodes.item(i)
            if(subnode.nodeName == value)
                  return getValue(node)
      }

      return ""
}

fun initFromTemplate(fileNameAndPath: String, templatePath: String) {
      val template = File(templatePath)
      if(!template.exists())
            throw IllegalArgumentException("Template not found.")

      val destFile = File(fileNameAndPath)
      if(destFile.exists()) {
            throw IllegalArgumentException("File already exists.")
      }

      template.copyTo(destFile, false)
}

fun saveFile(doc: Document, path: String) {
      val transformerFactory = TransformerFactory.newInstance()
      val transformer = transformerFactory.newTransformer()
      val source = DOMSource(doc)
      val result = StreamResult(File(path))
      transformer.transform(source, result)
}

private fun getValue(node: Node): String {
      if(node.attributes.getNamedItem("type") != null) {
            val parentName = node.attributes.getNamedItem("parent").nodeValue
            val root = getRootNode(node)

            val parentNode = root.findChild(parentName)
            if(parentNode == null)
                  return "LOOKUP ERROR: WRONG PARENT NAME"

            val subNode = parentNode.findChild(node.textContent)
            if(subNode == null)
                  return "LOOKUP ERROR: WRONG VALUE"

            val valNode = subNode.findChild(node.nodeName)
            if(valNode == null)
                  return "LOOKUP ERROR: WRONG ELEMENT"

            return getValue(valNode)


      } else {
            return node.textContent
      }
}

private fun getRootNode(node: Node): Node {
      var root = node;
      while(root.parentNode != null)
            root = root.parentNode

      root = root.childNodes.item(0)

      return root
}