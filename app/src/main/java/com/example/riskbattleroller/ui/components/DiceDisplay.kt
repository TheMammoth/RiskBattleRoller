package com.example.riskbattleroller.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.riskbattleroller.R

@Composable
fun DiceView(
    value: Int,
    isWinner: Boolean?,
    isAnimating: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = when {
            isAnimating -> 1f
            isWinner == true -> 1.15f
            isWinner == false -> 0.9f
            else -> 1f
        },
        animationSpec = tween(durationMillis = 200),
        label = "dice_scale"
    )

    val tintColor = when {
        isAnimating -> null
        isWinner == true -> Color.Green.copy(alpha = 0.4f)
        isWinner == false -> Color.Red.copy(alpha = 0.5f)
        else -> null
    }

    Image(
        painter = painterResource(id = getDiceDrawable(value)),
        contentDescription = "Dice showing $value",
        modifier = modifier
            .size(90.dp)
            .padding(6.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colorFilter = tintColor?.let { ColorFilter.tint(it, BlendMode.SrcAtop) }
    )
}

@Composable
fun DiceRow(
    diceValues: List<Int>,
    diceWins: List<Boolean>,
    isAnimating: Boolean,
    maxDice: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until maxDice) {
            if (i < diceValues.size) {
                DiceView(
                    value = diceValues[i],
                    isWinner = if (i < diceWins.size) diceWins[i] else null,
                    isAnimating = isAnimating
                )
            }
        }
    }
}

private fun getDiceDrawable(value: Int): Int = when (value) {
    1 -> R.drawable.dice1
    2 -> R.drawable.dice2
    3 -> R.drawable.dice3
    4 -> R.drawable.dice4
    5 -> R.drawable.dice5
    6 -> R.drawable.dice6
    else -> R.drawable.dice1
}
