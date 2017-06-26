package com.leftyist.kingdomsim.utils

import com.leftyist.kingdomsim.ai.findBestBuilding
import com.leftyist.kingdomsim.structures.Kingdom
import org.junit.jupiter.api.Test
import org.w3c.dom.Element

internal class XMLParserKtTest {
      @Test
      fun openFileTest() {
            val buildingsDoc = openFile("data/newbuildings.xml")
            val rows = buildingsDoc.getElementsByTagName("row")

            for(i in 0..rows.length - 1) {
                  val building = rows.item(i)
                  if(building == null || building.nodeValue == "#text")
                        continue

                  for(j in 0..building.childNodes.length - 1) {
                        val stat = building.childNodes.item(j)

                        if(stat == null || stat.textContent == null )
                              continue

                        if(stat.nodeName == "cost") {
                              (building as Element).setAttribute("cost", stat.textContent)
                              building.removeChild(stat)
                        } else if(stat.nodeName == "turns") {
                              (building as Element).setAttribute("turns", stat.textContent)
                              building.removeChild(stat)
                        }
                  }
            }

            saveFile(buildingsDoc, "data/output.xml")
      }

      @Test
      fun rename() {
            val buildingsDoc = openFile("data/output.xml")
            val rows = buildingsDoc.getElementsByTagName("row")

            for(i in 0..rows.length - 1) {
                  val building = rows.item(i)
                  if(building == null || building.nodeValue == "#text")
                        continue

                  val name = building.findChild("Name")?.textContent?.replace(" ", "")
                  if(name != building.nodeName) {
                        buildingsDoc.renameNode(building, building.namespaceURI, name)
                        println("Renamed " + name)
                  }


                  for(j in 0..building.childNodes.length - 1) {
                        val stat = building.childNodes.item(j)

                        if(stat == null || stat.textContent == null )
                              continue

                        if(stat.nodeName == "Name")
                              building.removeChild(stat)
                  }
            }

            saveFile(buildingsDoc, "data/output.xml")
      }

      @Test
      fun buildingTest() {
            val pq = findBestBuilding("stability", "village")
            for(pair in pq.sortedDescending()) {
                  println(pair.name + ": " + pair.rank)
            }
      }

      @Test
      fun miscTest() {
            val filepath = "saves/kingdoms/Prenad.xml"
            //val templatePath = "data/templates/t_kingdom.xml"
            //initFromTemplate(filepath, templatePath)

            val kingdom = Kingdom(filepath)
            kingdom.addSettlement("BigCity")
            //kingdom.save()
            //kingdom.addBuilding("BigCity", "House")
            kingdom.modifyKingdomStat("bp", 5)
            kingdom.modifyKingdomStat("stability", 10)
            kingdom.modifyKingdomStat("economy", 2)
            kingdom.modifyKingdomStat("consumption", 2)


            repeat(3) {
                  kingdom.onNextTurn()
                  kingdom.printStat("bp")
                  kingdom.printStat("unrest")
                  println("----------------------")
            }


      }
}