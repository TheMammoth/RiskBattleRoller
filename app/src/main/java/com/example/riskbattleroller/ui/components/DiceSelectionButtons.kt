package com.example.riskbattleroller.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DiceSelectionButtons(
    counts: List<Int>,
    selectedCount: Int,
    onCountSelected: (Int) -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        counts.forEach { count ->
            val isSelected = count == selectedCount
            Button(
                onClick = { onCountSelected(count) },
                modifier = Modifier
                    .width(70.dp)
                    .padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) selectedColor else unselectedColor
                )
            ) {
                Text(
                    text = count.toString(),
                    fontSize = if (isSelected) 28.sp else 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}
