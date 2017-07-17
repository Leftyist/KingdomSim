package com.leftyist.kingdomsim.structures

import com.leftyist.kingdomsim.ai.determineWhatToBuild
import com.leftyist.kingdomsim.utils.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import sun.rmi.runtime.Log
import java.util.logging.Level
import java.util.logging.Logger

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

      data class buildingInConstruction(val settlement: String, val building: String, val cost: Int, val turns: Int)

      fun getBuildQueue(): ArrayList<buildingInConstruction> {
            val buildQueue = ArrayList<buildingInConstruction>()

            val queueNode = kingdomDoc.getElementsByTagName("BuildingQueue").item(0)

            for(i in 0..queueNode.childNodes.length - 1) {
                  val node = queueNode.childNodes.item(i)
                  if(node == null || node.nodeName == null || node.nodeName == "#text")
                        continue

                  val cost = node.attributes.getNamedItem("cost").nodeValue.toInt()
                  val turns = node.attributes.getNamedItem("turns").nodeValue.toInt()
                  val settlementName = node.attributes.getNamedItem("settlement").nodeValue
                  val building = node.nodeName

                  val bundle = buildingInConstruction(settlementName, building, cost, turns)
                  buildQueue.add(bundle)
            }
            return buildQueue
      }

      fun processBuildQueue() {
            val buildQueue = kingdomDoc.getElementsByTagName("BuildingQueue").item(0)
            for(i in 0..buildQueue.childNodes.length - 1) {
                  val node = buildQueue.childNodes.item(i)
                  if(node == null || node.nodeName == null || node.nodeName == "#text")
                        continue

                  val cost = node.attributes.getNamedItem("cost").nodeValue.toInt()
                  val turns = node.attributes.getNamedItem("turns")
                  modifyKingdomStat("bp", -cost)
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

      fun getKingdomStat(stat: String): Int {
            val stats = kingdomDoc.getElementsByTagName("Stats").item(0)
            val node = stats.findChild(stat)
            if(node == null)
                  throw IllegalArgumentException("Kingdom stat doesn't exist.")

            return node.textContent.toInt()
      }

      fun getKingdomStatString(stat: String): String {
            val stats = kingdomDoc.getElementsByTagName("Stats").item(0)
            val node = stats.findChild(stat)
            if(node == null)
                  throw IllegalArgumentException("Kingdom stat doesn't exist.")

            if(node.textContent == null)
                  return ""

            return node.textContent
      }

      fun modifyKingdomStat(stat: String, value: Int) {
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

      private fun addBuildingStats(buildingNode: Node, settlementName: String) {
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

      private fun upkeepPhase() {

            //STEP 1
            val stability = getKingdomStat("stability") - getKingdomStat("unrest")
            val dc = getKingdomStat("controldc")
            val roll = roll("1d20")

            //fail
            if(roll == 1 || roll + stability <= dc) {
                  if(dc - roll - stability < 5)
                        modifyKingdomStat("unrest", 1)
                  else
                        modifyKingdomStat("unrest", roll("1d4"))
            }
            //succeed
            else {
                  val unrest = getKingdomStat("unrest")
                  if(unrest > 0)
                        modifyKingdomStat("unrest", -1)
                  else
                        modifyKingdomStat("bp", 1)
            }

            //STEP 2
            modifyKingdomStat("bp", -getKingdomStat("consumption"))

            //STEP 3
            //TODO: roll magic items

            //STEP 4
            if(getKingdomStat("economy") < 0)
                  modifyKingdomStat("unrest", 1)

            if(getKingdomStat("stability") < 0)
                  modifyKingdomStat("unrest", 1)

            if(getKingdomStat("loyalty") < 0)
                  modifyKingdomStat("unrest", 1)

            //STEP 5
            //TODO: If the kingdom’s Unrest is 11 or higher, it loses 1 hex (the leaders choose which hex). See Losing Hexes.
            //If your kingdom’s Unrest ever reaches 20, the kingdom falls into anarchy.
            // While in anarchy, your kingdom can take no action and treats all Economy, Loyalty,
            // and Stability check results as 0. Restoring order once a kingdom falls into anarchy
            // typically requires a number of quests and lengthy adventures by you and the other
            // would-be leaders to restore the people’s faith in you.
      }

      private fun edictPhase() {
            //STEP 1
            //TODO: Assign leadership

            //STEP 2
            //TODO: Claim or abandon hexes

            //STEP 3
            //TODO: Build terrain improvements

            //STEP 4
            //TODO: Create and improve settlements (build stuff)
            determineWhatToBuild(this)

            //STEP 5
            //TODO: Create army units

            //STEP 6
            //TODO: Issue edicts
      }

      private fun incomePhase() {
            //STEP 1
            //TODO: Personal withdrawal from the treasury

            //STEP 2
            //TODO: Personal deposits into the treasury

            //STEP 3
            //TODO: Sell expensive items for BP

            //STEP 4
            val economy = getKingdomStat("economy")
            val income = (roll("1d20") + economy)/3
            modifyKingdomStat("bp", income)
      }

      private fun eventPhase() {
            //TODO: roll some events
      }

      fun onNextTurn() {
            processBuildQueue()
            upkeepPhase()
            edictPhase()
            incomePhase()
            eventPhase()
      }

      fun printStat(stat: String) {
            println(stat.capitalize() + ": " + getKingdomStat(stat))
      }
}