package com.example.riskbattleroller.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riskbattleroller.ui.components.AttackerSection
import com.example.riskbattleroller.ui.components.BattleHistoryOverlay
import com.example.riskbattleroller.ui.components.DefenderSection
import com.example.riskbattleroller.ui.components.MiddleSection
import com.example.riskbattleroller.viewmodel.BattleViewModel

@Composable
fun RiskBattleRollerApp(
    viewModel: BattleViewModel = viewModel()
) {
    val battleState by viewModel.battleState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AttackerSection(
                diceCount = battleState.attackerDiceCount,
                diceValues = battleState.attackerDiceValues,
                attackerWins = battleState.attackerWins,
                isAnimating = battleState.isAnimating,
                flashState = battleState.flashState,
                onDiceCountSelected = viewModel::selectAttackerDice,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            MiddleSection(
                resultText = battleState.resultText,
                casualtiesText = "Total - Attacker: ${battleState.attackerCasualties} | Defender: ${battleState.defenderCasualties}",
                isBattling = battleState.isBattling,
                onBattleClick = viewModel::startBattle,
                onResetClick = viewModel::resetCasualties,
                onHistoryClick = viewModel::showHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            DefenderSection(
                diceCount = battleState.defenderDiceCount,
                diceValues = battleState.defenderDiceValues,
                defenderWins = battleState.defenderWins,
                isAnimating = battleState.isAnimating,
                flashState = battleState.flashState,
                onDiceCountSelected = viewModel::selectDefenderDice,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        if (battleState.isHistoryVisible) {
            BattleHistoryOverlay(
                history = battleState.battleHistory,
                onClose = viewModel::hideHistory
            )
        }
    }
}
