package com.example.riskbattleroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.riskbattleroller.ui.RiskBattleRollerApp
import com.example.riskbattleroller.ui.theme.RiskBattleRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RiskBattleRollerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RiskBattleRollerApp()
                }
            }
        }
    }
}
