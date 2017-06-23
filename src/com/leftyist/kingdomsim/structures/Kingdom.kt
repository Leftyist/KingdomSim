package com.leftyist.kingdomsim.structures

import com.leftyist.kingdomsim.utils.*
import org.w3c.dom.Element
import org.w3c.dom.Node

class Kingdom(val filepath: String) {
      val kingdomDoc = openFile(filepath)

      fun save() {
            saveFile(kingdomDoc, filepath)
      }

      fun addSettlement(settlementName: String) {
            val template = openFile("data/templates/t_settlement.xml")

            val settleRoot = kingdomDoc.createElement(settlementName)
            val statsNode = template.getElementsByTagName("Stats").item(0).cloneNode(true)
            val buildingsNode = template.getElementsByTagName("Buildings").item(0).cloneNode(true)

            kingdomDoc.adoptNode(statsNode)
            kingdomDoc.adoptNode(buildingsNode)

            settleRoot.appendChild(statsNode)
            settleRoot.appendChild(buildingsNode)

            val kingdomSettlements = kingdomDoc.firstChild.findChild("Settlements")
            kingdomSettlements!!.appendChild(settleRoot)
      }

      fun addBuilding(settlement: String, building: String) {
            val buildingsDoc = openFile("data/buildings.xml")
            val buildingNode = buildingsDoc.getElementsByTagName(building).item(0).cloneNode(true)
            (buildingNode as Element).setAttribute("settlement", settlement)

            val queue = kingdomDoc.getElementsByTagName("BuildingQueue").item(0)
            queue.appendChild(kingdomDoc.importNode(buildingNode, true))
      }

      fun processBuildQueue() {
            val buildQueue = kingdomDoc.getElementsByTagName("BuildingQueue").item(0)
            for(i in 0..buildQueue.childNodes.length - 1) {
                  val node = buildQueue.childNodes.item(i)
                  if(node == null || node.nodeName == null || node.nodeName == "#text")
                        continue

                  modifyKingdomStat("bp", -(node.attributes.getNamedItem("cost").nodeValue.toInt()))
                  val turns = node.attributes.getNamedItem("turns")
                  turns.nodeValue = (turns.nodeValue.toInt() - 1).toString() //"decrement" the string

                  //building is done
                  if(turns.nodeValue.toInt() <= 0) {
                        val removedNode = buildQueue.removeChild(node)
                        val settlementName = removedNode.attributes.getNamedItem("settlement").nodeValue
                        addBuildingStats(removedNode, settlementName)
                        (removedNode as Element).removeAttribute("settlement")
                        (removedNode as Element).removeAttribute("cost")
                        (removedNode as Element).removeAttribute("turns")
                        val settlement = kingdomDoc.getElementsByTagName(settlementName).item(0)
                        settlement.findChild("Buildings")!!.appendChild(removedNode)
                  }
            }
      }

      private fun modifyKingdomStat(stat: String, value: Int) {
            val stats = kingdomDoc.getElementsByTagName("Stats").item(0)
            val node = stats.findChild(stat)
            if(node == null)
                  throw IllegalArgumentException("Kingdom stat doesn't exist.")

            node.textContent = (node.textContent.toInt() + value).toString()
      }

      private fun modifySettlementStat(settlement: String, stat: String, value: Int) {
            val sett = kingdomDoc.getElementsByTagName(settlement).item(0)
            val stats = sett.findChild("Stats")
            val node = stats!!.findChild(stat)
            if(node == null)
                  throw IllegalArgumentException("Stat doesn't exist.")

            node.textContent = (node.textContent.toInt() + value).toString()
      }

      fun addBuildingStats(buildingNode: Node, settlementName: String) {
            val buildingStats = getNodeSubValues(buildingNode)
            val settlement = kingdomDoc.getElementsByTagName(settlementName).item(0)
            val settlementStats = settlement.findChild("Stats")
            val kingdomStats = kingdomDoc.getElementsByTagName("Stats").item(0)

            if(settlementStats == null || kingdomStats == null)
                  throw IllegalArgumentException("Settlement/Kingdom stats not found.")

            for(stats in buildingStats) {
                  for(i in 0..settlementStats.childNodes.length - 1) {
                        val subNode = settlementStats.childNodes.item(i)
                        if(subNode.nodeValue == "#text")
                              continue

                        if(subNode.nodeName == stats.first)
                              modifySettlementStat(settlementName, stats.first, stats.second.toInt())

                  }

                  for(i in 0..kingdomStats.childNodes.length - 1) {
                        val subNode = kingdomStats.childNodes.item(i)
                        if(subNode.nodeValue == "#text")
                              continue

                        if(subNode.nodeName == stats.first)
                              modifyKingdomStat(stats.first, stats.second.toInt())
                  }
            }
      }
}