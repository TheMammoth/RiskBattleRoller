package com.example.riskbattleroller.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riskbattleroller.ui.theme.DefenderBlue
import com.example.riskbattleroller.ui.theme.DefenderFlash

@Composable
fun DefenderSection(
    diceCount: Int,
    diceValues: List<Int>,
    defenderWins: List<Boolean>,
    isAnimating: Boolean,
    flashState: Boolean,
    onDiceCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (flashState && isAnimating) DefenderFlash else DefenderBlue,
        animationSpec = tween(durationMillis = 100),
        label = "defender_background"
    )

    Surface(
        modifier = modifier,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            DiceRow(
                diceValues = diceValues,
                diceWins = defenderWins,
                isAnimating = isAnimating,
                maxDice = 2,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            DiceSelectionButtons(
                counts = listOf(1, 2),
                selectedCount = diceCount,
                onCountSelected = onDiceCountSelected,
                selectedColor = Color(0xFFE3F2FD),
                unselectedColor = Color(0xFF0D47A1),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "DEFENDER",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
