package com.leftyist.kingdomsim.utils

import com.leftyist.kingdomsim.structures.Kingdom
import org.junit.jupiter.api.Test

internal class XMLParserKtTest {
      @Test
      fun openFileTest() {
            
      }

      @Test
      fun miscTest() {
            val filepath = "saves/kingdoms/Prenad.xml"
            val templatePath = "data/templates/t_kingdom.xml"
            initFromTemplate(filepath, templatePath)

            val kingdom = Kingdom(filepath)
            kingdom.addSettlement("Colony")
            //kingdom.save()
            kingdom.addBuilding("Colony", "House")
            kingdom.processBuildQueue()
            kingdom.save()
      }
}