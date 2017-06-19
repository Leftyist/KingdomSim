package com.leftyist.kingdomsim.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DiceKtTest {
    @Test
    fun rollRange() {
        repeat(20) {
            val roll = rollRange(max = 20)
            assertTrue(1 <= roll && roll <= 20)
        }

        repeat(20) {
            val roll = rollRange(20, 40)
            assertTrue(20 <= roll && roll <= 40)
        }

    }

    @Test
    fun roll() {

        repeat(20) {
            val roll = roll("1d4")
            assertTrue(1 <= roll && roll <= 4)
        }

        repeat(20) {
            val roll = roll("1d6 - 2")
            assertTrue(1 <= roll && roll <= 4)
        }

        repeat(20) {
            val roll = roll("2d6 + 4")
            assertTrue(6 <= roll && roll <= 16)
        }

        repeat(20) {
            val roll = roll("2d6 + 1d12 - 3d10 + 20 - 10")
            assertTrue(1 <= roll && roll <= 33)
        }
    }

}