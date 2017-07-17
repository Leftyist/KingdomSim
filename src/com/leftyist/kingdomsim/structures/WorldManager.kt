package com.leftyist.kingdomsim.structures

object WorldManager {
      val kingdoms = ArrayList<Kingdom>()

      fun addKingdom(k: Kingdom) {
            kingdoms.add(k)
      }

      fun getKingdom(name: String): Kingdom? {
            for(kingdom in kingdoms) {
                  if(kingdom.getKingdomStatString("name") == name)
                        return kingdom
            }
            return null
      }

      fun onTurnChange() {
            for (kingdom in kingdoms){
                  kingdom.onNextTurn()
            }
      }
}