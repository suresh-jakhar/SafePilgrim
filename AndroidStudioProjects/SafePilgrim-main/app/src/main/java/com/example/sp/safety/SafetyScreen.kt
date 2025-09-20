package com.example.sp.safety

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SafetyScreen(onBack: () -> Unit) {
    val areaRisk = remember { mutableIntStateOf(3) }
    val isNight = remember { mutableStateOf(false) }
    val isSolo = remember { mutableStateOf(true) }
    val incident = remember { mutableIntStateOf(20) }
    val weather = remember { mutableIntStateOf(10) }

    val score = SafetyCalculator.calculate(
        SafetyInputs(
            areaRiskLevel = areaRisk.intValue,
            isNightTime = isNight.value,
            isSoloTravel = isSolo.value,
            incidentHistoryScore = incident.intValue,
            weatherRiskScore = weather.intValue
        )
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Score: ${'$'}{score.score} (${score.label})",
            modifier = Modifier.background(score.color).padding(8.dp),
            color = Color.White
        )

        Button(onClick = onBack) { Text("Back") }
    }
}


