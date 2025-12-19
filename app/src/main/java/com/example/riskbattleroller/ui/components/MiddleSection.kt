package com.example.riskbattleroller.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riskbattleroller.ui.theme.ButtonGray
import com.example.riskbattleroller.ui.theme.MiddleGold

@Composable
fun MiddleSection(
    resultText: String,
    casualtiesText: String,
    isBattling: Boolean,
    onBattleClick: () -> Unit,
    onResetClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MiddleGold
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onBattleClick,
                enabled = !isBattling,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF57C00)
                )
            ) {
                Text(
                    text = "BATTLE!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (resultText.isNotEmpty()) {
                Text(
                    text = resultText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Text(
                text = casualtiesText,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onResetClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonGray
                    )
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = onHistoryClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonGray
                    )
                ) {
                    Text("History")
                }
            }
        }
    }
}
