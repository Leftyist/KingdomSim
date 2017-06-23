package com.leftyist.kingdomsim.utils

import com.leftyist.kingdomsim.structures.Kingdom
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.w3c.dom.Node
import com.sun.xml.internal.ws.addressing.EndpointReferenceUtil.transform
import java.io.File
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory

internal class XMLParserKtTest {
      @Test
      fun openFileTest() {

      }

      @Test
      fun miscTest() {
            val filepath = "saves/kingdoms/Prenad.xml"
            val templatePath = "data/templates/t_kingdom.xml"
            //initFromTemplate(filepath, templatePath)

            val kingdom = Kingdom(filepath)
            //kingdom.addSettlement("Colony")
            //kingdom.save()
            //kingdom.addBuilding("Colony", "House")
            kingdom.processBuildQueue()
            kingdom.save()
      }
}