package com.example.sp.safety

import androidx.compose.ui.graphics.Color

data class SafetyInputs(
    val areaRiskLevel: Int,              // 1 (low) .. 5 (high)
    val isNightTime: Boolean,            // night increases risk
    val isSoloTravel: Boolean,           // solo increases risk
    val incidentHistoryScore: Int,       // 0 (none) .. 100 (many)
    val weatherRiskScore: Int            // 0 (good) .. 100 (bad)
)

data class SafetyScore(val score: Int, val color: Color, val label: String)

object SafetyCalculator {
    // Produces score in range 0..100 where higher is safer
    fun calculate(inputs: SafetyInputs): SafetyScore {
        val areaPenalty = (inputs.areaRiskLevel - 1) * 12  // 0..48
        val nightPenalty = if (inputs.isNightTime) 10 else 0
        val soloPenalty = if (inputs.isSoloTravel) 10 else 0
        val historyPenalty = (inputs.incidentHistoryScore * 0.3).toInt() // 0..30
        val weatherPenalty = (inputs.weatherRiskScore * 0.3).toInt()     // 0..30

        val penalties = areaPenalty + nightPenalty + soloPenalty + historyPenalty + weatherPenalty
        val raw = 100 - penalties
        val bounded = raw.coerceIn(0, 100)

        val (color, label) = when {
            bounded >= 70 -> Color(0xFF2E7D32) to "Low"
            bounded >= 40 -> Color(0xFFF9A825) to "Moderate"
            else -> Color(0xFFC62828) to "High"
        }
        return SafetyScore(bounded, color, label)
    }
}


