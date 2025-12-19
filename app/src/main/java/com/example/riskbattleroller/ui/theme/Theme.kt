package com.example.riskbattleroller.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BattleButtonOrange,
    secondary = AttackerRed,
    tertiary = DefenderBlue
)

@Composable
fun RiskBattleRollerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
