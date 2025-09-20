package com.example.sp.ai

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.math.*

class EnhancedSafetyCalculator {
    
    suspend fun calculateWithAI(
        inputs: SafetyInputs,
        realTimeData: RealTimeData,
        userBehavior: UserBehavior
    ): EnhancedSafetyScore = withContext(Dispatchers.IO) {
        
        // Base safety score calculation
        val baseScore = calculateBaseScore(inputs)
        
        // AI-enhanced adjustments
        val aiAdjustments = calculateAIAdjustments(realTimeData, userBehavior)
        
        // Behavioral pattern analysis
        val behaviorScore = analyzeBehaviorPatterns(userBehavior)
        
        // Real-time risk factors
        val realTimeRisk = calculateRealTimeRisk(realTimeData)
        
        // Combine all factors
        val finalScore = combineScores(baseScore, aiAdjustments, behaviorScore, realTimeRisk)
        
        // Generate enhanced recommendations
        val recommendations = generateEnhancedRecommendations(inputs, realTimeData, userBehavior, finalScore)
        
        // Calculate confidence level
        val confidence = calculateConfidence(inputs, realTimeData, userBehavior)
        
        EnhancedSafetyScore(
            score = finalScore,
            baseScore = baseScore,
            aiAdjustments = aiAdjustments,
            behaviorScore = behaviorScore,
            realTimeRisk = realTimeRisk,
            color = getScoreColor(finalScore),
            label = getScoreLabel(finalScore),
            recommendations = recommendations,
            confidence = confidence,
            timestamp = LocalDateTime.now()
        )
    }
    
    private fun calculateBaseScore(inputs: SafetyInputs): Double {
        var score = 100.0
        
        // Area risk penalty (0-48 points)
        score -= (inputs.areaRiskLevel - 1) * 12.0
        
        // Night time penalty
        if (inputs.isNightTime) score -= 10.0
        
        // Solo travel penalty
        if (inputs.isSoloTravel) score -= 10.0
        
        // Incident history penalty (0-30 points)
        score -= inputs.incidentHistoryScore * 0.3
        
        // Weather risk penalty (0-30 points)
        score -= inputs.weatherRiskScore * 0.3
        
        return score.coerceIn(0.0, 100.0)
    }
    
    private fun calculateAIAdjustments(realTimeData: RealTimeData, userBehavior: UserBehavior): Double {
        var adjustment = 0.0
        
        // Crowd density adjustment
        when {
            realTimeData.crowdDensity > 90 -> adjustment -= 15.0
            realTimeData.crowdDensity > 70 -> adjustment -= 10.0
            realTimeData.crowdDensity < 30 -> adjustment += 5.0
        }
        
        // Weather conditions adjustment
        when (realTimeData.weatherCondition) {
            WeatherCondition.STORM -> adjustment -= 20.0
            WeatherCondition.HEAVY_RAIN -> adjustment -= 15.0
            WeatherCondition.LIGHT_RAIN -> adjustment -= 5.0
            WeatherCondition.CLEAR -> adjustment += 5.0
            WeatherCondition.UNKNOWN -> adjustment -= 2.0
        }
        
        // Time-based adjustments
        val currentHour = LocalDateTime.now().hour
        when {
            currentHour in 22..23 || currentHour in 0..5 -> adjustment -= 15.0
            currentHour in 6..8 -> adjustment -= 5.0
            currentHour in 9..17 -> adjustment += 5.0
        }
        
        // User behavior adjustments
        if (userBehavior.lastCommunicationTime.isBefore(LocalDateTime.now().minusHours(6))) {
            adjustment -= 10.0
        }
        
        if (userBehavior.lastCheckInTime.isBefore(LocalDateTime.now().minusHours(24))) {
            adjustment -= 15.0
        }
        
        return adjustment
    }
    
    private fun analyzeBehaviorPatterns(userBehavior: UserBehavior): Double {
        var behaviorScore = 50.0 // Neutral starting point
        
        // Communication pattern analysis
        val communicationGap = java.time.Duration.between(
            userBehavior.lastCommunicationTime,
            LocalDateTime.now()
        ).toHours()
        
        when {
            communicationGap < 2 -> behaviorScore += 10.0
            communicationGap < 6 -> behaviorScore += 5.0
            communicationGap > 12 -> behaviorScore -= 15.0
            communicationGap > 24 -> behaviorScore -= 25.0
        }
        
        // App usage pattern analysis
        val appUsageGap = java.time.Duration.between(
            userBehavior.lastAppUsageTime,
            LocalDateTime.now()
        ).toHours()
        
        when {
            appUsageGap < 4 -> behaviorScore += 5.0
            appUsageGap > 24 -> behaviorScore -= 10.0
        }
        
        // Check-in pattern analysis
        val checkInGap = java.time.Duration.between(
            userBehavior.lastCheckInTime,
            LocalDateTime.now()
        ).toHours()
        
        when {
            checkInGap < 12 -> behaviorScore += 10.0
            checkInGap > 48 -> behaviorScore -= 20.0
        }
        
        return behaviorScore.coerceIn(0.0, 100.0)
    }
    
    private fun calculateRealTimeRisk(realTimeData: RealTimeData): Double {
        var riskScore = 0.0
        
        // Traffic conditions
        when (realTimeData.trafficCondition) {
            TrafficCondition.HEAVY -> riskScore += 15.0
            TrafficCondition.MODERATE -> riskScore += 8.0
            TrafficCondition.LIGHT -> riskScore += 3.0
            TrafficCondition.NONE -> riskScore += 0.0
        }
        
        // Public transport status
        when (realTimeData.publicTransportStatus) {
            PublicTransportStatus.DISRUPTED -> riskScore += 20.0
            PublicTransportStatus.DELAYED -> riskScore += 10.0
            PublicTransportStatus.NORMAL -> riskScore += 0.0
        }
        
        // Emergency services availability
        when (realTimeData.emergencyServicesStatus) {
            EmergencyServicesStatus.LIMITED -> riskScore += 25.0
            EmergencyServicesStatus.NORMAL -> riskScore += 0.0
        }
        
        // Local events
        if (realTimeData.hasLocalEvents) {
            riskScore += 10.0
        }
        
        // Political situation
        when (realTimeData.politicalSituation) {
            PoliticalSituation.UNSTABLE -> riskScore += 30.0
            PoliticalSituation.TENSE -> riskScore += 15.0
            PoliticalSituation.STABLE -> riskScore += 0.0
        }
        
        return riskScore
    }
    
    private fun combineScores(
        baseScore: Double,
        aiAdjustments: Double,
        behaviorScore: Double,
        realTimeRisk: Double
    ): Double {
        // Weighted combination
        val weightedBase = baseScore * 0.4
        val weightedAI = aiAdjustments * 0.3
        val weightedBehavior = behaviorScore * 0.2
        val weightedRisk = realTimeRisk * 0.1
        
        val combinedScore = weightedBase + weightedAI + weightedBehavior - weightedRisk
        
        return combinedScore.coerceIn(0.0, 100.0)
    }
    
    private fun generateEnhancedRecommendations(
        inputs: SafetyInputs,
        realTimeData: RealTimeData,
        userBehavior: UserBehavior,
        finalScore: Double
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        // Base recommendations based on score
        when {
            finalScore >= 80 -> {
                recommendations.add("Excellent safety conditions - enjoy your trip!")
                recommendations.add("Consider sharing your location with trusted contacts")
            }
            finalScore >= 60 -> {
                recommendations.add("Good safety conditions with minor precautions needed")
                recommendations.add("Stay alert and aware of your surroundings")
            }
            finalScore >= 40 -> {
                recommendations.add("Moderate risk - extra caution recommended")
                recommendations.add("Avoid traveling alone if possible")
                recommendations.add("Keep emergency contacts readily available")
            }
            else -> {
                recommendations.add("High risk conditions - consider postponing travel")
                recommendations.add("Travel with a companion")
                recommendations.add("Stay in well-lit, populated areas")
                recommendations.add("Keep emergency services on speed dial")
            }
        }
        
        // Real-time specific recommendations
        if (realTimeData.crowdDensity > 80) {
            recommendations.add("High crowd density - watch for pickpockets and stay alert")
        }
        
        if (realTimeData.weatherCondition == WeatherCondition.STORM) {
            recommendations.add("Severe weather - avoid outdoor activities")
        }
        
        if (realTimeData.trafficCondition == TrafficCondition.HEAVY) {
            recommendations.add("Heavy traffic - allow extra travel time")
        }
        
        if (realTimeData.publicTransportStatus == PublicTransportStatus.DISRUPTED) {
            recommendations.add("Public transport disrupted - have alternative transport ready")
        }
        
        // Behavioral recommendations
        val communicationGap = java.time.Duration.between(
            userBehavior.lastCommunicationTime,
            LocalDateTime.now()
        ).toHours()
        
        if (communicationGap > 6) {
            recommendations.add("Consider checking in with family/friends")
        }
        
        val checkInGap = java.time.Duration.between(
            userBehavior.lastCheckInTime,
            LocalDateTime.now()
        ).toHours()
        
        if (checkInGap > 24) {
            recommendations.add("Time for a safety check-in")
        }
        
        // Time-based recommendations
        val currentHour = LocalDateTime.now().hour
        if (currentHour in 22..23 || currentHour in 0..5) {
            recommendations.add("Late night hours - extra caution recommended")
        }
        
        return recommendations.distinct()
    }
    
    private fun calculateConfidence(
        inputs: SafetyInputs,
        realTimeData: RealTimeData,
        userBehavior: UserBehavior
    ): Double {
        var confidence = 0.5 // Base confidence
        
        // Data completeness factors
        if (inputs.areaRiskLevel > 0) confidence += 0.1
        if (inputs.incidentHistoryScore > 0) confidence += 0.1
        if (inputs.weatherRiskScore > 0) confidence += 0.1
        if (realTimeData.crowdDensity > 0) confidence += 0.1
        if (realTimeData.weatherCondition != WeatherCondition.UNKNOWN) confidence += 0.1
        
        // Behavioral data factors
        val communicationGap = java.time.Duration.between(
            userBehavior.lastCommunicationTime,
            LocalDateTime.now()
        ).toHours()
        
        if (communicationGap < 24) confidence += 0.1
        
        return confidence.coerceIn(0.0, 1.0)
    }
    
    private fun getScoreColor(score: Double): Color {
        return when {
            score >= 80 -> Color(0xFF2E7D32) // Green
            score >= 60 -> Color(0xFF388E3C) // Light Green
            score >= 40 -> Color(0xFFF9A825) // Orange
            else -> Color(0xFFC62828) // Red
        }
    }
    
    private fun getScoreLabel(score: Double): String {
        return when {
            score >= 80 -> "Excellent"
            score >= 60 -> "Good"
            score >= 40 -> "Moderate"
            else -> "High Risk"
        }
    }
}

data class SafetyInputs(
    val areaRiskLevel: Int,              // 1 (low) .. 5 (high)
    val isNightTime: Boolean,            // night increases risk
    val isSoloTravel: Boolean,           // solo increases risk
    val incidentHistoryScore: Int,       // 0 (none) .. 100 (many)
    val weatherRiskScore: Int            // 0 (good) .. 100 (bad)
)

data class RealTimeData(
    val crowdDensity: Double,            // 0-100
    val weatherCondition: WeatherCondition,
    val trafficCondition: TrafficCondition,
    val publicTransportStatus: PublicTransportStatus,
    val emergencyServicesStatus: EmergencyServicesStatus,
    val hasLocalEvents: Boolean,
    val politicalSituation: PoliticalSituation
)

enum class WeatherCondition {
    CLEAR, LIGHT_RAIN, HEAVY_RAIN, STORM, UNKNOWN
}

enum class TrafficCondition {
    NONE, LIGHT, MODERATE, HEAVY
}

enum class PublicTransportStatus {
    NORMAL, DELAYED, DISRUPTED
}

enum class EmergencyServicesStatus {
    NORMAL, LIMITED
}

enum class PoliticalSituation {
    STABLE, TENSE, UNSTABLE
}

data class EnhancedSafetyScore(
    val score: Double,
    val baseScore: Double,
    val aiAdjustments: Double,
    val behaviorScore: Double,
    val realTimeRisk: Double,
    val color: Color,
    val label: String,
    val recommendations: List<String>,
    val confidence: Double,
    val timestamp: LocalDateTime
)
