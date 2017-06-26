package com.leftyist.kingdomsim.structures

class WorldManager {
      val kingdoms = ArrayList<Kingdom>()

      init {

      }

      fun addKingdom() {

      }

      fun onTurnChange() {
            for (kingdom in kingdoms)
                  kingdom.onNextTurn()
      }
}