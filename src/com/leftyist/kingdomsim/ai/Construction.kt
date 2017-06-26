package com.leftyist.kingdomsim.ai

import com.leftyist.kingdomsim.structures.Kingdom
import com.leftyist.kingdomsim.utils.findChild
import com.leftyist.kingdomsim.utils.openFile
import java.util.*

fun calcTurnNetIncome(kingdom: Kingdom, turnsAhead: Int): Int {
      val economy = kingdom.getKingdomStat("economy")
      val consumption = kingdom.getKingdomStat("consumption")

      var queueCost = 0

      val buildQueue = kingdom.kingdomDoc.getElementsByTagName("BuildingQueue").item(0)
      for (i in 0..buildQueue.childNodes.length - 1) {
            val node = buildQueue.childNodes.item(i)
            if (node == null || node.nodeName == null || node.nodeName == "#text")
                  continue

            val cost = node.attributes.getNamedItem("cost").nodeValue.toInt()
            val turns = node.attributes.getNamedItem("turns").nodeValue.toInt()

            if(turns - turnsAhead > 0)
                  queueCost += cost
      }

      return (economy + 10) / 3 - consumption - queueCost
}

fun determineWhatToBuild(kingdom: Kingdom) {
      val stability = kingdom.getKingdomStat("stability")
      val controldc = kingdom.getKingdomStat("controldc")

      //low stability, very dangeorus
      if(controldc - stability > 10) {
            //determine best value stability building

      }
}

private fun buildingType(type: String): Int {
      when(type){
            "village" -> return 0
            "town" -> return 1
            "city" -> return 2
            else -> return 0
      }
}

fun Pair<String, Double>.compare() {

}

class BuildingPair(val name: String, val rank: Double) : Comparable<BuildingPair> {
      override fun compareTo(other: BuildingPair): Int {
            if(this.rank < other.rank)
                  return -1
            else if(this.rank == other.rank)
                  return 0
            else
                  return 1
      }
}

fun findBestBuilding(statToLookFor: String, settlementType: String): PriorityQueue<BuildingPair> {
      val buildingsDoc = openFile("data/buildings.xml")

      val buildings = buildingsDoc.firstChild.childNodes
      val maxPQ = PriorityQueue<BuildingPair>(10, Collections.reverseOrder())
      for(i in 0..buildings.length) {
            val building = buildings.item(i)
            if(building == null || building.nodeName == null || building.nodeName == "#text")
                  continue

            val targetStat = building.findChild(statToLookFor)
            if(targetStat == null)
                  continue

            val type = building.findChild("type")
            if(type == null || buildingType(type.textContent) > buildingType(settlementType))
                  continue

            val cost = building.attributes.getNamedItem("cost").nodeValue.toInt()
            val turns = building.attributes.getNamedItem("turns").nodeValue.toInt()

            val ranking: Double = targetStat.textContent.toDouble() / (cost*turns)
            maxPQ.add(BuildingPair(building.nodeName, ranking))
      }

      return maxPQ
}