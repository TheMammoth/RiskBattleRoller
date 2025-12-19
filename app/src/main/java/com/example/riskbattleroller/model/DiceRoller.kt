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
        // Create pairs of (value, originalIndex) and sort by value descending
        val attackerIndexed = attackerValues.mapIndexed { index, value -> value to index }
            .sortedByDescending { it.first }
        val defenderIndexed = defenderValues.mapIndexed { index, value -> value to index }
            .sortedByDescending { it.first }

        val attackerWins = MutableList(attackerValues.size) { false }
        val defenderWins = MutableList(defenderValues.size) { false }

        var attackerLosses = 0
        var defenderLosses = 0

        val comparisons = minOf(attackerIndexed.size, defenderIndexed.size)

        for (i in 0 until comparisons) {
            val attackerValue = attackerIndexed[i].first
            val attackerOriginalIndex = attackerIndexed[i].second
            val defenderValue = defenderIndexed[i].first
            val defenderOriginalIndex = defenderIndexed[i].second

            if (attackerValue > defenderValue) {
                defenderLosses++
                attackerWins[attackerOriginalIndex] = true
                defenderWins[defenderOriginalIndex] = false
            } else {
                // Ties favor defender in Risk
                attackerLosses++
                attackerWins[attackerOriginalIndex] = false
                defenderWins[defenderOriginalIndex] = true
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
