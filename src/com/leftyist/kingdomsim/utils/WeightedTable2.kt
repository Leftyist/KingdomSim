package com.leftyist.kingdomsim.utils

import org.w3c.dom.NodeList

class WeightedTable2 (nodes: NodeList){

      private val options = ArrayList<WeightedOption>()
      private var totalWeight = 0

      class WeightedOption(val weightBound: Int, val name: String, val values: ArrayList<Pair<String, String>>) {

            fun print() {
                  System.out.printf(name + "\t w(%d)", weightBound)
                  println("\n-----------------------")

                  var maxLength = 0;
                  for(pair in values) {
                        if(pair.first.length > maxLength)
                              maxLength = pair.first.length
                  }

                  for(pair in values) {
                        print(pair.first)
                        repeat(maxLength+5 -pair.first.length) {
                              print(".")
                        }
                        println(pair.second)
                  }
            }
      }

      init {
            for(i in 0..nodes.length - 1) {
                  val node = nodes.item(i)
                  if(node.nodeName == "#text") //looking for WeightedOption
                        continue

                  else if (node.nodeName == "WeightedOption") {
                        val name = node.attributes.getNamedItem("name").nodeValue
                        val weight = node.attributes.getNamedItem("weight").nodeValue
                        addOption(weight.toInt(), name, getNodeSubValues(node.childNodes))
                  }
            }
      }

      fun addOption(weight: Int, name: String, values: ArrayList<Pair<String, String>>) {
            totalWeight += weight;
            options.add(WeightedOption(totalWeight, name, values))
      }

      fun rollOption(): WeightedOption {
            val num = rollRange(max = totalWeight)
            for (option in options) {
                  if (num <= option.weightBound)
                        return option
            }

            return WeightedOption(0, "ERROR", ArrayList<Pair<String, String>>())
      }

      fun print() {
            println("Options: " + options.size + "\t Total Weight: " + totalWeight)
            println("===============================")
            for(option in options){
                  option.print()
                  println()
            }
      }
}