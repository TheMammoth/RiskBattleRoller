package com.example.riskbattleroller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.riskbattleroller.model.BattleHistoryEntry
import com.example.riskbattleroller.model.BattleState
import com.example.riskbattleroller.model.DiceRoller
import com.example.riskbattleroller.model.SoundManager
import com.example.riskbattleroller.util.DiceAnimator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BattleViewModel(application: Application) : AndroidViewModel(application) {
    private val _battleState = MutableStateFlow(BattleState())
    val battleState: StateFlow<BattleState> = _battleState.asStateFlow()

    private val soundManager = SoundManager(application.applicationContext)
    private val diceRoller = DiceRoller()

    fun selectAttackerDice(count: Int) {
        _battleState.update { it.copy(attackerDiceCount = count) }
    }

    fun selectDefenderDice(count: Int) {
        _battleState.update { it.copy(defenderDiceCount = count) }
    }

    fun startBattle() {
        if (_battleState.value.isBattling) return

        viewModelScope.launch {
            _battleState.update {
                it.copy(
                    isBattling = true,
                    isAnimating = true,
                    attackerWins = emptyList(),
                    defenderWins = emptyList(),
                    resultText = ""
                )
            }
            soundManager.playDiceRoll()

            DiceAnimator.animateDiceRoll(
                attackerCount = _battleState.value.attackerDiceCount,
                defenderCount = _battleState.value.defenderDiceCount,
                onFrame = { attackerVals, defenderVals, flash ->
                    _battleState.update {
                        it.copy(
                            attackerDiceValues = attackerVals,
                            defenderDiceValues = defenderVals,
                            flashState = flash
                        )
                    }
                },
                onComplete = { finalAttacker, finalDefender ->
                    calculateBattleResult(finalAttacker, finalDefender)
                    _battleState.update {
                        it.copy(
                            isBattling = false,
                            isAnimating = false,
                            flashState = false
                        )
                    }
                }
            )
        }
    }

    private fun calculateBattleResult(attackerValues: List<Int>, defenderValues: List<Int>) {
        val result = diceRoller.calculateBattle(attackerValues, defenderValues)

        val newBattleNumber = _battleState.value.battleNumber + 1
        val historyEntry = BattleHistoryEntry(
            battleNumber = newBattleNumber,
            attackerValues = attackerValues.sortedDescending(),
            defenderValues = defenderValues.sortedDescending(),
            result = result.resultText
        )

        _battleState.update {
            it.copy(
                attackerDiceValues = attackerValues,
                defenderDiceValues = defenderValues,
                attackerCasualties = it.attackerCasualties + result.attackerLosses,
                defenderCasualties = it.defenderCasualties + result.defenderLosses,
                attackerWins = result.attackerWins,
                defenderWins = result.defenderWins,
                resultText = result.resultText,
                battleNumber = newBattleNumber,
                battleHistory = it.battleHistory + historyEntry
            )
        }
    }

    fun resetCasualties() {
        _battleState.update {
            it.copy(
                attackerCasualties = 0,
                defenderCasualties = 0,
                battleNumber = 0,
                battleHistory = emptyList(),
                resultText = ""
            )
        }
    }

    fun showHistory() {
        _battleState.update { it.copy(isHistoryVisible = true) }
    }

    fun hideHistory() {
        _battleState.update { it.copy(isHistoryVisible = false) }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
