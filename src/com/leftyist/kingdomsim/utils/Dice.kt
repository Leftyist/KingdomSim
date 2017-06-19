package com.leftyist.kingdomsim.utils;

import java.util.concurrent.ThreadLocalRandom

private fun recursiveParse(input: String): Int {

      val plus = input.indexOf("+")
      val minus = input.indexOf("-")

      var splitPoint = 0

      if (minus == -1 && plus == -1)
            return parsePiece(input)
      else if (minus != -1 && plus != -1)
            splitPoint = minOf(minus, plus)
      else
            splitPoint = maxOf(minus, plus)

      val left = input.substring(0, splitPoint)
      val right = input.substring(splitPoint + 1, input.length)

      if (input[splitPoint] == '+')
            return parsePiece(left) + recursiveParse(right)
      else
            return parsePiece(left) - recursiveParse(right)
}

private fun parsePiece(input: String): Int {
      if (input.contains("d")) {
            return rollDice(input);
      } else {
            return input.toInt();
      }
}

private fun rollDice(dice: String): Int {
      val halves = dice.split("d")

      var sum = 0;
      repeat(halves[0].toInt()) {
            sum += rollRange(max = halves[1].toInt())
      }
      return sum;
}

fun rollRange(min: Int = 1, max: Int): Int {
      return ThreadLocalRandom.current().nextInt(min, max + 1)
}

fun roll(roll: String): Int {
      val result = recursiveParse(roll.replace(" ", ""))
      if (result < 1)
            return 1
      else
            return result
}