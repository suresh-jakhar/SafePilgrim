package com.example.sp.ai

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.*

class RiskPredictor(private val context: Context) {
    
    private val historicalData = mutableMapOf<String, List<HistoricalIncident>>()
    private val weatherData = mutableMapOf<String, WeatherForecast>()
    private val crowdData = mutableMapOf<String, CrowdDensityData>()
    
    suspend fun predictRisk(
        location: android.location.Location,
        timeWindow: TimeWindow,
        userProfile: UserProfile
    ): RiskPrediction = withContext(Dispatchers.IO) {
        
        try {
            // Gather relevant data
            val historicalIncidents = getHistoricalIncidents(location, timeWindow)
            val weatherForecast = getWeatherForecast(location, timeWindow)
            val crowdDensity = getCrowdDensity(location, timeWindow)
            val localEvents = getLocalEvents(location, timeWindow)
            
            // Calculate risk factors
            val historicalRisk = calculateHistoricalRisk(historicalIncidents)
            val weatherRisk = calculateWeatherRisk(weatherForecast)
            val crowdRisk = calculateCrowdRisk(crowdDensity)
            val eventRisk = calculateEventRisk(localEvents)
            val timeRisk = calculateTimeRisk(timeWindow)
            val userRisk = calculateUserRisk(userProfile)
            
            // Combine risk factors
            val totalRisk = combineRiskFactors(
                historicalRisk, weatherRisk, crowdRisk, eventRisk, timeRisk, userRisk
            )
            
            // Generate predictions
            val riskLevel = determineRiskLevel(totalRisk)
            val recommendations = generatePredictiveRecommendations(
                location, timeWindow, userProfile, totalRisk
            )
            
            // Calculate confidence
            val confidence = calculatePredictionConfidence(
                historicalIncidents, weatherForecast, crowdDensity
            )
            
            RiskPrediction(
                location = location,
                timeWindow = timeWindow,
                riskScore = totalRisk,
                riskLevel = riskLevel,
                factors = RiskFactors(
                    weatherRisk = weatherRisk,
                    crowdDensity = crowdRisk,
                    isNightTime = timeRisk > 30.0,
                    isSoloTravel = userRisk > 20.0,
                    areaRiskLevel = historicalRisk,
                    historicalIncidents = eventRisk
                ),
                recommendations = recommendations,
                confidence = confidence,
                timestamp = LocalDateTime.now()
            )
            
        } catch (e: Exception) {
            Log.e("RiskPredictor", "Error predicting risk", e)
            RiskPrediction(
                location = location,
                timeWindow = timeWindow,
                riskScore = 0.0,
                riskLevel = RiskLevel.LOW,
                factors = RiskFactors(
                    weatherRisk = 0.0,
                    crowdDensity = 0.0,
                    isNightTime = false,
                    isSoloTravel = false,
                    areaRiskLevel = 0.0,
                    historicalIncidents = 0.0
                ),
                recommendations = emptyList(),
                confidence = 0.0,
                timestamp = LocalDateTime.now()
            )
        }
    }
    
    suspend fun predictRouteRisk(
        route: List<android.location.Location>,
        timeWindow: TimeWindow,
        userProfile: UserProfile
    ): RouteRiskPrediction = withContext(Dispatchers.IO) {
        
        val segmentRisks = mutableListOf<SegmentRisk>()
        var totalRisk = 0.0
        
        for (i in 0 until route.size - 1) {
            val segmentStart = route[i]
            val segmentEnd = route[i + 1]
            val segmentMidpoint = calculateMidpoint(segmentStart, segmentEnd)
            
            val segmentRisk = predictRisk(segmentMidpoint, timeWindow, userProfile)
            segmentRisks.add(
                SegmentRisk(
                    start = segmentStart,
                    end = segmentEnd,
                    risk = segmentRisk
                )
            )
            
            totalRisk += segmentRisk.riskScore
        }
        
        val averageRisk = totalRisk / segmentRisks.size
        val maxRisk = segmentRisks.maxOfOrNull { it.risk.riskScore } ?: 0.0
        
        RouteRiskPrediction(
            route = route,
            timeWindow = timeWindow,
            segmentRisks = segmentRisks,
            averageRisk = averageRisk,
            maxRisk = maxRisk,
            overallRiskLevel = determineRiskLevel(averageRisk),
            recommendations = generateRouteRecommendations(segmentRisks),
            timestamp = LocalDateTime.now()
        )
    }
    
    suspend fun getRiskTrends(
        location: android.location.Location,
        timeRange: TimeRange
    ): Flow<RiskTrend> = flow {
        
        val trends = mutableListOf<RiskTrendPoint>()
        
        for (hour in 0 until timeRange.durationInHours) {
            val timeWindow = TimeWindow(
                startTime = timeRange.startTime.plusHours(hour.toLong()),
                endTime = timeRange.startTime.plusHours(hour + 1L)
            )
            
            val risk = predictRisk(location, timeWindow, UserProfile())
            trends.add(
                RiskTrendPoint(
                    time = timeWindow.startTime,
                    riskScore = risk.riskScore,
                    riskLevel = risk.riskLevel
                )
            )
        }
        
        emit(RiskTrend(location = location, trends = trends))
    }
    
    private fun getHistoricalIncidents(location: android.location.Location, timeWindow: TimeWindow): List<HistoricalIncident> {
        // In production, this would query a real database
        // For demo purposes, return mock data
        return listOf(
            HistoricalIncident(
                location = location,
                incidentType = IncidentType.THEFT,
                severity = IncidentSeverity.MEDIUM,
                timestamp = LocalDateTime.now().minusDays(1),
                description = "Pickpocket incident reported"
            ),
            HistoricalIncident(
                location = location,
                incidentType = IncidentType.ACCIDENT,
                severity = IncidentSeverity.LOW,
                timestamp = LocalDateTime.now().minusDays(3),
                description = "Minor traffic accident"
            )
        )
    }
    
    private fun getWeatherForecast(location: android.location.Location, timeWindow: TimeWindow): WeatherForecast {
        // In production, this would query a weather API
        return WeatherForecast(
            location = location,
            timeWindow = timeWindow,
            condition = WeatherCondition.CLEAR,
            temperature = 25.0,
            humidity = 60.0,
            windSpeed = 10.0,
            precipitation = 0.0
        )
    }
    
    private fun getCrowdDensity(location: android.location.Location, timeWindow: TimeWindow): CrowdDensityData {
        // In production, this would query real-time crowd data
        return CrowdDensityData(
            location = location,
            timeWindow = timeWindow,
            density = 70.0, // 0-100
            trend = CrowdTrend.INCREASING
        )
    }
    
    private fun getLocalEvents(location: android.location.Location, timeWindow: TimeWindow): List<LocalEvent> {
        // In production, this would query event databases
        return listOf(
            LocalEvent(
                name = "Festival",
                location = location,
                timeWindow = timeWindow,
                expectedCrowd = 1000,
                eventType = EventType.FESTIVAL
            )
        )
    }
    
    private fun calculateHistoricalRisk(incidents: List<HistoricalIncident>): Double {
        if (incidents.isEmpty()) return 0.0
        
        val totalRisk = incidents.sumOf { incident ->
            when (incident.severity) {
                IncidentSeverity.LOW -> 10.0
                IncidentSeverity.MEDIUM -> 25.0
                IncidentSeverity.HIGH -> 50.0
                IncidentSeverity.CRITICAL -> 100.0
            }
        }
        
        return (totalRisk / incidents.size).coerceIn(0.0, 100.0)
    }
    
    private fun calculateWeatherRisk(forecast: WeatherForecast): Double {
        var risk = 0.0
        
        when (forecast.condition) {
            WeatherCondition.CLEAR -> risk = 0.0
            WeatherCondition.LIGHT_RAIN -> risk = 15.0
            WeatherCondition.HEAVY_RAIN -> risk = 30.0
            WeatherCondition.STORM -> risk = 60.0
            WeatherCondition.UNKNOWN -> risk = 20.0
        }
        
        // Temperature extremes
        if (forecast.temperature > 35 || forecast.temperature < 5) {
            risk += 10.0
        }
        
        // High wind
        if (forecast.windSpeed > 20) {
            risk += 15.0
        }
        
        return risk.coerceIn(0.0, 100.0)
    }
    
    private fun calculateCrowdRisk(crowdData: CrowdDensityData): Double {
        return when {
            crowdData.density > 90 -> 40.0
            crowdData.density > 70 -> 25.0
            crowdData.density > 50 -> 15.0
            crowdData.density > 30 -> 5.0
            else -> 0.0
        }
    }
    
    private fun calculateEventRisk(events: List<LocalEvent>): Double {
        if (events.isEmpty()) return 0.0
        
        val totalRisk = events.sumOf { event ->
            when (event.eventType) {
                EventType.FESTIVAL -> 30.0
                EventType.CONCERT -> 25.0
                EventType.SPORTS -> 20.0
                EventType.PROTEST -> 60.0
                EventType.OTHER -> 15.0
            }
        }
        
        return (totalRisk / events.size).coerceIn(0.0, 100.0)
    }
    
    private fun calculateTimeRisk(timeWindow: TimeWindow): Double {
        val hour = timeWindow.startTime.hour
        
        return when {
            hour in 22..23 || hour in 0..5 -> 40.0
            hour in 6..8 -> 15.0
            hour in 9..17 -> 5.0
            hour in 18..21 -> 20.0
            else -> 10.0
        }
    }
    
    private fun calculateUserRisk(userProfile: UserProfile): Double {
        var risk = 0.0
        
        if (userProfile.isSoloTraveler) risk += 20.0
        if (userProfile.isFirstTimeVisitor) risk += 15.0
        if (userProfile.age < 18 || userProfile.age > 65) risk += 10.0
        if (userProfile.hasMedicalConditions) risk += 25.0
        
        return risk.coerceIn(0.0, 100.0)
    }
    
    private fun combineRiskFactors(
        historical: Double,
        weather: Double,
        crowd: Double,
        event: Double,
        time: Double,
        user: Double
    ): Double {
        // Weighted combination
        val weightedHistorical = historical * 0.25
        val weightedWeather = weather * 0.20
        val weightedCrowd = crowd * 0.15
        val weightedEvent = event * 0.15
        val weightedTime = time * 0.15
        val weightedUser = user * 0.10
        
        return (weightedHistorical + weightedWeather + weightedCrowd + 
                weightedEvent + weightedTime + weightedUser).coerceIn(0.0, 100.0)
    }
    
    private fun determineRiskLevel(riskScore: Double): RiskLevel {
        return when {
            riskScore >= 70 -> RiskLevel.HIGH
            riskScore >= 40 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
    
    private fun generatePredictiveRecommendations(
        location: android.location.Location,
        timeWindow: TimeWindow,
        userProfile: UserProfile,
        totalRisk: Double
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        when (determineRiskLevel(totalRisk)) {
            RiskLevel.HIGH -> {
                recommendations.add("Consider postponing travel to this area")
                recommendations.add("Travel with a companion if possible")
                recommendations.add("Stay in well-lit, populated areas")
                recommendations.add("Keep emergency contacts readily available")
            }
            RiskLevel.MEDIUM -> {
                recommendations.add("Exercise extra caution")
                recommendations.add("Stay alert and aware of surroundings")
                recommendations.add("Avoid isolated areas")
            }
            RiskLevel.LOW -> {
                recommendations.add("Standard safety precautions apply")
                recommendations.add("Enjoy your visit safely")
            }
            RiskLevel.CRITICAL -> {
                recommendations.add("Do not travel to this area")
                recommendations.add("Contact local authorities immediately")
                recommendations.add("Seek alternative routes")
            }
        }
        
        // Time-specific recommendations
        val hour = timeWindow.startTime.hour
        if (hour in 22..23 || hour in 0..5) {
            recommendations.add("Late night hours - extra caution recommended")
        }
        
        // User-specific recommendations
        if (userProfile.isSoloTraveler) {
            recommendations.add("Consider joining a group tour")
        }
        
        if (userProfile.isFirstTimeVisitor) {
            recommendations.add("Familiarize yourself with the area beforehand")
        }
        
        return recommendations.distinct()
    }
    
    private fun generateRouteRecommendations(segmentRisks: List<SegmentRisk>): List<String> {
        val recommendations = mutableListOf<String>()
        
        val highRiskSegments = segmentRisks.filter { it.risk.riskLevel == RiskLevel.HIGH }
        if (highRiskSegments.isNotEmpty()) {
            recommendations.add("Avoid high-risk segments: ${highRiskSegments.size} identified")
        }
        
        val averageRisk = segmentRisks.map { it.risk.riskScore }.average()
        if (averageRisk > 50) {
            recommendations.add("Consider alternative route")
        }
        
        return recommendations
    }
    
    private fun calculatePredictionConfidence(
        incidents: List<HistoricalIncident>,
        weather: WeatherForecast,
        crowd: CrowdDensityData
    ): Double {
        var confidence = 0.5 // Base confidence
        
        if (incidents.isNotEmpty()) confidence += 0.2
        if (weather.condition != WeatherCondition.UNKNOWN) confidence += 0.2
        if (crowd.density > 0) confidence += 0.1
        
        return confidence.coerceIn(0.0, 1.0)
    }
    
    private fun calculateMidpoint(location1: android.location.Location, location2: android.location.Location): android.location.Location {
        val midpoint = android.location.Location("midpoint")
        midpoint.latitude = (location1.latitude + location2.latitude) / 2
        midpoint.longitude = (location1.longitude + location2.longitude) / 2
        return midpoint
    }
}

// Location class removed to avoid conflicts with android.location.Location

// TimeWindow class removed to avoid conflicts

data class TimeRange(
    val startTime: LocalDateTime,
    val durationInHours: Int
)

data class UserProfile(
    val isSoloTraveler: Boolean = true,
    val isFirstTimeVisitor: Boolean = false,
    val age: Int = 30,
    val hasMedicalConditions: Boolean = false
)

data class HistoricalIncident(
    val location: android.location.Location,
    val incidentType: IncidentType,
    val severity: IncidentSeverity,
    val timestamp: LocalDateTime,
    val description: String
)

enum class IncidentType {
    THEFT, ACCIDENT, MEDICAL_EMERGENCY, SECURITY_THREAT, NATURAL_DISASTER, OTHER
}

enum class IncidentSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}

data class WeatherForecast(
    val location: android.location.Location,
    val timeWindow: TimeWindow,
    val condition: WeatherCondition,
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val precipitation: Double
)

data class CrowdDensityData(
    val location: android.location.Location,
    val timeWindow: TimeWindow,
    val density: Double, // 0-100
    val trend: CrowdTrend
)

enum class CrowdTrend {
    INCREASING, DECREASING, STABLE
}

data class LocalEvent(
    val name: String,
    val location: android.location.Location,
    val timeWindow: TimeWindow,
    val expectedCrowd: Int,
    val eventType: EventType
)

enum class EventType {
    FESTIVAL, CONCERT, SPORTS, PROTEST, OTHER
}

// RiskPrediction class removed to avoid conflicts

// RiskFactors class removed to avoid conflicts

data class RouteRiskPrediction(
    val route: List<android.location.Location>,
    val timeWindow: TimeWindow,
    val segmentRisks: List<SegmentRisk>,
    val averageRisk: Double,
    val maxRisk: Double,
    val overallRiskLevel: RiskLevel,
    val recommendations: List<String>,
    val timestamp: LocalDateTime
)

data class SegmentRisk(
    val start: android.location.Location,
    val end: android.location.Location,
    val risk: RiskPrediction
)

data class RiskTrend(
    val location: android.location.Location,
    val trends: List<RiskTrendPoint>
)

data class RiskTrendPoint(
    val time: LocalDateTime,
    val riskScore: Double,
    val riskLevel: RiskLevel
)
