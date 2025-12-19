package com.example.riskbattleroller.util

import kotlinx.coroutines.delay
import kotlin.random.Random

class DiceAnimator {
    companion object {
        const val ANIMATION_DURATION = 750L
        const val FRAME_DELAY = 100L

        suspend fun animateDiceRoll(
            attackerCount: Int,
            defenderCount: Int,
            onFrame: (attackerValues: List<Int>, defenderValues: List<Int>, flashState: Boolean) -> Unit,
            onComplete: (attackerValues: List<Int>, defenderValues: List<Int>) -> Unit
        ) {
            val random = Random.Default
            val startTime = System.currentTimeMillis()

            // Generate final values
            val finalAttacker = List(attackerCount) { random.nextInt(1, 7) }
            val finalDefender = List(defenderCount) { random.nextInt(1, 7) }

            var flashState = false

            while (System.currentTimeMillis() - startTime < ANIMATION_DURATION) {
                val randomAttacker = List(attackerCount) { random.nextInt(1, 7) }
                val randomDefender = List(defenderCount) { random.nextInt(1, 7) }

                onFrame(randomAttacker, randomDefender, flashState)
                flashState = !flashState

                delay(FRAME_DELAY)
            }

            // Final frame
            onComplete(finalAttacker, finalDefender)
        }
    }
}
