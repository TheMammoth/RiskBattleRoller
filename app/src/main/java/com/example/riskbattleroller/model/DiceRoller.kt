package com.example.riskbattleroller.model

data class BattleResult(
    val attackerLosses: Int,
    val defenderLosses: Int,
    val attackerWins: List<Boolean>,
    val defenderWins: List<Boolean>,
    val resultText: String
)

class DiceRoller {
    fun calculateBattle(attackerValues: List<Int>, defenderValues: List<Int>): BattleResult {
        // Sort in descending order (highest first)
        val attackerSorted = attackerValues.sortedDescending()
        val defenderSorted = defenderValues.sortedDescending()

        val attackerWins = MutableList(attackerSorted.size) { false }
        val defenderWins = MutableList(defenderSorted.size) { false }

        var attackerLosses = 0
        var defenderLosses = 0

        val comparisons = minOf(attackerSorted.size, defenderSorted.size)

        for (i in 0 until comparisons) {
            if (attackerSorted[i] > defenderSorted[i]) {
                defenderLosses++
                attackerWins[i] = true
                defenderWins[i] = false
            } else {
                // Ties favor defender in Risk
                attackerLosses++
                attackerWins[i] = false
                defenderWins[i] = true
            }
        }

        val resultText = when {
            attackerLosses > defenderLosses -> "Attacker loses $attackerLosses army!"
            defenderLosses > attackerLosses -> "Defender loses $defenderLosses army!"
            else -> "Both lose $attackerLosses army!"
        }

        return BattleResult(
            attackerLosses = attackerLosses,
            defenderLosses = defenderLosses,
            attackerWins = attackerWins,
            defenderWins = defenderWins,
            resultText = resultText
        )
    }
}
