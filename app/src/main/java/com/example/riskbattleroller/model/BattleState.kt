package com.example.riskbattleroller.model

data class BattleState(
    val attackerDiceCount: Int = 1,
    val defenderDiceCount: Int = 1,
    val attackerDiceValues: List<Int> = emptyList(),
    val defenderDiceValues: List<Int> = emptyList(),
    val attackerCasualties: Int = 0,
    val defenderCasualties: Int = 0,
    val battleHistory: List<BattleHistoryEntry> = emptyList(),
    val battleNumber: Int = 0,
    val resultText: String = "",
    val isBattling: Boolean = false,
    val isHistoryVisible: Boolean = false,
    val attackerWins: List<Boolean> = emptyList(),
    val defenderWins: List<Boolean> = emptyList(),
    val isAnimating: Boolean = false,
    val flashState: Boolean = false
)

data class BattleHistoryEntry(
    val battleNumber: Int,
    val attackerValues: List<Int>,
    val defenderValues: List<Int>,
    val result: String
)
