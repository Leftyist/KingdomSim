package com.leftyist.kingdomsim.utils

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class WeightedTableTest {


      @BeforeEach
      fun setUp() {
            val list = ArrayList<Pair<String, String>>()
            list.add(Pair("One", "Cool"))
            list.add(Pair("Two", "Neat"))
            list.add(Pair("Three", "Wow"))

            //table.addOption(10, "First Option", list)

            val list2 = ArrayList<Pair<String, String>>()
            list2.add(Pair("Ein", "Kool"))
            list2.add(Pair("Zwei", "Super"))
            list2.add(Pair("Drei", "Vow"))

            //table.addOption(20, "Second Option", list2)
      }
}