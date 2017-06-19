package com.leftyist.kingdomsim.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.w3c.dom.Node
import org.w3c.dom.NodeList

internal class XMLParser2KtTest {
      @Test
      fun openFileTest() {
            val doc = openFile("data/test.xml")
            val nodes = findChildByName(doc?.childNodes, "Buildings")

            if(nodes == null) {
                  fail("shit aint there")
                  return;
            }

            val table = WeightedTable2(nodes)
            //table.print()

            table.rollOption().print()
            println()

            table.rollOption().print()
            println()

            table.rollOption().print()
            println()
      }

}