package com.example.sp.ai

import android.content.Context
import android.location.Location
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.*

// Data classes will be defined locally to avoid import issues

class AnomalyDetector(private val context: Context) {
    
    private val locationHistory = mutableListOf<LocationData>()
    private val behaviorPatterns = mutableMapOf<String, BehaviorPattern>()
    private val riskFactors = mutableMapOf<String, RiskFactor>()
    
    suspend fun detectLocationAnomaly(locationHistory: List<LocationData>): AnomalyResult = withContext(Dispatchers.IO) {
        try {
            val anomalies = mutableListOf<Anomaly>()
            
            // Detect speed anomalies
            anomalies.addAll(detectSpeedAnomalies(locationHistory))
            
            // Detect route deviations
            anomalies.addAll(detectRouteDeviations(locationHistory))
            
            // Detect location drop-offs
            anomalies.addAll(detectLocationDropoffs(locationHistory))
            
            // Detect prolonged inactivity
            anomalies.addAll(detectProlongedInactivity(locationHistory))
            
            val severity = calculateAnomalySeverity(anomalies)
            
            AnomalyResult(
                anomalies = anomalies,
                severity = severity,
                confidence = calculateConfidence(anomalies),
                timestamp = LocalDateTime.now()
            )
        } catch (e: Exception) {
            Log.e("AnomalyDetector", "Error detecting location anomalies", e)
            AnomalyResult(
                anomalies = emptyList(),
                severity = AnomalySeverity.LOW,
                confidence = 0.0,
                timestamp = LocalDateTime.now()
            )
        }
    }
    
    suspend fun detectBehavioralAnomaly(userBehavior: UserBehavior): AnomalyResult = withContext(Dispatchers.IO) {
        try {
            val anomalies = mutableListOf<Anomaly>()
            
            // Detect communication pattern changes
            anomalies.addAll(detectCommunicationAnomalies(userBehavior))
            
            // Detect app usage pattern changes
            anomalies.addAll(detectAppUsageAnomalies(userBehavior))
            
            // Detect check-in frequency changes
            anomalies.addAll(detectCheckInAnomalies(userBehavior))
            
            val severity = calculateAnomalySeverity(anomalies)
            
            AnomalyResult(
                anomalies = anomalies,
                severity = severity,
                confidence = calculateConfidence(anomalies),
                timestamp = LocalDateTime.now()
            )
        } catch (e: Exception) {
            Log.e("AnomalyDetector", "Error detecting behavioral anomalies", e)
            AnomalyResult(
                anomalies = emptyList(),
                severity = AnomalySeverity.LOW,
                confidence = 0.0,
                timestamp = LocalDateTime.now()
            )
        }
    }
    
    suspend fun predictRisk(factors: RiskFactors): RiskPrediction = withContext(Dispatchers.IO) {
        try {
            val riskScore = calculateRiskScore(factors)
            val riskLevel = determineRiskLevel(riskScore)
            val recommendations = generateRecommendations(factors, riskLevel)
            
            RiskPrediction(
                location = android.location.Location("default").apply { 
                    latitude = 0.0; longitude = 0.0 
                },
                timeWindow = TimeWindow(LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
                riskScore = riskScore,
                riskLevel = riskLevel,
                factors = factors,
                recommendations = recommendations,
                confidence = calculateRiskConfidence(factors),
                timestamp = LocalDateTime.now()
            )
        } catch (e: Exception) {
            Log.e("AnomalyDetector", "Error predicting risk", e)
            RiskPrediction(
                location = android.location.Location("default").apply { 
                    latitude = 0.0; longitude = 0.0 
                },
                timeWindow = TimeWindow(LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
                riskScore = 0.0,
                riskLevel = RiskLevel.LOW,
                factors = factors,
                recommendations = emptyList(),
                confidence = 0.0,
                timestamp = LocalDateTime.now()
            )
        }
    }
    
    private fun detectSpeedAnomalies(locationHistory: List<LocationData>): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        if (locationHistory.size < 2) return anomalies
        
        for (i in 1 until locationHistory.size) {
            val current = locationHistory[i]
            val previous = locationHistory[i - 1]
            
            val distance = calculateDistance(previous, current)
            val timeDiff = ChronoUnit.SECONDS.between(previous.timestamp, current.timestamp)
            
            if (timeDiff > 0) {
                val speed = distance / timeDiff // m/s
                val speedKmh = speed * 3.6 // km/h
                
                // Detect unusually high speed (> 200 km/h for tourists)
                if (speedKmh > 200) {
                    anomalies.add(
                        Anomaly(
                            type = AnomalyType.SPEED_ANOMALY,
                            severity = AnomalySeverity.HIGH,
                            description = "Unusually high speed detected: ${speedKmh.toInt()} km/h",
                            location = current.location,
                            timestamp = current.timestamp,
                            confidence = 0.9
                        )
                    )
                }
                
                // Detect unusually low speed (< 1 km/h for extended period)
                if (speedKmh < 1 && timeDiff > 3600) { // 1 hour
                    anomalies.add(
                        Anomaly(
                            type = AnomalyType.SPEED_ANOMALY,
                            severity = AnomalySeverity.MEDIUM,
                            description = "Prolonged low speed detected: ${speedKmh.toInt()} km/h for ${timeDiff / 3600} hours",
                            location = current.location,
                            timestamp = current.timestamp,
                            confidence = 0.7
                        )
                    )
                }
            }
        }
        
        return anomalies
    }
    
    private fun detectRouteDeviations(locationHistory: List<LocationData>): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        // Simple route deviation detection
        // In production, this would use more sophisticated algorithms
        if (locationHistory.size < 3) return anomalies
        
        val recentLocations = locationHistory.takeLast(3)
        val deviation = calculateRouteDeviation(recentLocations)
        
        if (deviation > 1000) { // 1km deviation
            anomalies.add(
                Anomaly(
                    type = AnomalyType.ROUTE_DEVIATION,
                    severity = AnomalySeverity.MEDIUM,
                    description = "Significant route deviation detected: ${deviation.toInt()}m",
                    location = recentLocations.last().location,
                    timestamp = recentLocations.last().timestamp,
                    confidence = 0.8
                )
            )
        }
        
        return anomalies
    }
    
    private fun detectLocationDropoffs(locationHistory: List<LocationData>): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        if (locationHistory.isEmpty()) return anomalies
        
        val lastLocation = locationHistory.last()
        val timeSinceLastUpdate = ChronoUnit.HOURS.between(lastLocation.timestamp, LocalDateTime.now())
        
        if (timeSinceLastUpdate > 4) { // 4 hours without location update
            anomalies.add(
                Anomaly(
                    type = AnomalyType.LOCATION_DROPOFF,
                    severity = AnomalySeverity.HIGH,
                    description = "No location updates for ${timeSinceLastUpdate} hours",
                    location = lastLocation.location,
                    timestamp = lastLocation.timestamp,
                    confidence = 0.9
                )
            )
        }
        
        return anomalies
    }
    
    private fun detectProlongedInactivity(locationHistory: List<LocationData>): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        if (locationHistory.size < 2) return anomalies
        
        val recentLocations = locationHistory.takeLast(10)
        val inactivityPeriods = mutableListOf<Long>()
        
        for (i in 1 until recentLocations.size) {
            val timeDiff = ChronoUnit.HOURS.between(
                recentLocations[i - 1].timestamp,
                recentLocations[i].timestamp
            )
            inactivityPeriods.add(timeDiff)
        }
        
        val maxInactivity = inactivityPeriods.maxOrNull() ?: 0
        
        if (maxInactivity > 6) { // 6 hours of inactivity
            anomalies.add(
                Anomaly(
                    type = AnomalyType.PROLONGED_INACTIVITY,
                    severity = AnomalySeverity.MEDIUM,
                    description = "Prolonged inactivity detected: ${maxInactivity} hours",
                    location = recentLocations.last().location,
                    timestamp = recentLocations.last().timestamp,
                    confidence = 0.8
                )
            )
        }
        
        return anomalies
    }
    
    private fun detectCommunicationAnomalies(userBehavior: UserBehavior): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        // Detect communication silence
        val timeSinceLastCommunication = ChronoUnit.HOURS.between(
            userBehavior.lastCommunicationTime,
            LocalDateTime.now()
        )
        
        if (timeSinceLastCommunication > 6) {
            anomalies.add(
                Anomaly(
                    type = AnomalyType.COMMUNICATION_SILENCE,
                    severity = AnomalySeverity.MEDIUM,
                    description = "No communication for ${timeSinceLastCommunication} hours",
                    location = null,
                    timestamp = userBehavior.lastCommunicationTime,
                    confidence = 0.7
                )
            )
        }
        
        return anomalies
    }
    
    private fun detectAppUsageAnomalies(userBehavior: UserBehavior): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        // Detect unusual app usage patterns
        val timeSinceLastAppUsage = ChronoUnit.HOURS.between(
            userBehavior.lastAppUsageTime,
            LocalDateTime.now()
        )
        
        if (timeSinceLastAppUsage > 12) {
            anomalies.add(
                Anomaly(
                    type = AnomalyType.APP_USAGE_ANOMALY,
                    severity = AnomalySeverity.LOW,
                    description = "No app usage for ${timeSinceLastAppUsage} hours",
                    location = null,
                    timestamp = userBehavior.lastAppUsageTime,
                    confidence = 0.6
                )
            )
        }
        
        return anomalies
    }
    
    private fun detectCheckInAnomalies(userBehavior: UserBehavior): List<Anomaly> {
        val anomalies = mutableListOf<Anomaly>()
        
        // Detect missed check-ins
        val timeSinceLastCheckIn = ChronoUnit.HOURS.between(
            userBehavior.lastCheckInTime,
            LocalDateTime.now()
        )
        
        if (timeSinceLastCheckIn > 24) {
            anomalies.add(
                Anomaly(
                    type = AnomalyType.MISSED_CHECKIN,
                    severity = AnomalySeverity.MEDIUM,
                    description = "No check-in for ${timeSinceLastCheckIn} hours",
                    location = null,
                    timestamp = userBehavior.lastCheckInTime,
                    confidence = 0.8
                )
            )
        }
        
        return anomalies
    }
    
    private fun calculateAnomalySeverity(anomalies: List<Anomaly>): AnomalySeverity {
        if (anomalies.isEmpty()) return AnomalySeverity.LOW
        
        val highSeverityCount = anomalies.count { it.severity == AnomalySeverity.HIGH }
        val mediumSeverityCount = anomalies.count { it.severity == AnomalySeverity.MEDIUM }
        
        return when {
            highSeverityCount > 0 -> AnomalySeverity.HIGH
            mediumSeverityCount > 1 -> AnomalySeverity.MEDIUM
            else -> AnomalySeverity.LOW
        }
    }
    
    private fun calculateConfidence(anomalies: List<Anomaly>): Double {
        if (anomalies.isEmpty()) return 0.0
        
        val totalConfidence = anomalies.sumOf { it.confidence }
        return totalConfidence / anomalies.size
    }
    
    private fun calculateRiskScore(factors: RiskFactors): Double {
        var score = 0.0
        
        // Weather risk (0-100)
        score += factors.weatherRisk * 0.2
        
        // Crowd density (0-100)
        score += factors.crowdDensity * 0.15
        
        // Time of day (0-100)
        score += if (factors.isNightTime) 30.0 else 0.0
        
        // Solo travel (0-100)
        score += if (factors.isSoloTravel) 25.0 else 0.0
        
        // Area risk level (0-100)
        score += factors.areaRiskLevel * 20.0
        
        // Historical incidents (0-100)
        score += factors.historicalIncidents * 0.1
        
        return score.coerceIn(0.0, 100.0)
    }
    
    private fun determineRiskLevel(riskScore: Double): RiskLevel {
        return when {
            riskScore >= 70 -> RiskLevel.HIGH
            riskScore >= 40 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
    
    private fun generateRecommendations(factors: RiskFactors, riskLevel: RiskLevel): List<String> {
        val recommendations = mutableListOf<String>()
        
        when (riskLevel) {
            RiskLevel.HIGH -> {
                recommendations.add("Avoid traveling alone")
                recommendations.add("Stay in well-lit areas")
                recommendations.add("Keep emergency contacts updated")
                recommendations.add("Consider postponing travel")
            }
            RiskLevel.MEDIUM -> {
                recommendations.add("Travel with a companion if possible")
                recommendations.add("Stay alert and aware of surroundings")
                recommendations.add("Keep phone charged and accessible")
            }
            RiskLevel.LOW -> {
                recommendations.add("Enjoy your trip safely")
                recommendations.add("Keep basic safety precautions")
            }
            RiskLevel.CRITICAL -> {
                recommendations.add("Do not travel to this area")
                recommendations.add("Contact local authorities immediately")
                recommendations.add("Seek alternative routes")
            }
        }
        
        if (factors.isNightTime) {
            recommendations.add("Extra caution recommended at night")
        }
        
        if (factors.crowdDensity > 80) {
            recommendations.add("High crowd density - watch for pickpockets")
        }
        
        return recommendations
    }
    
    private fun calculateRiskConfidence(factors: RiskFactors): Double {
        // Simple confidence calculation based on data completeness
        var confidence = 0.5 // Base confidence
        
        if (factors.weatherRisk > 0) confidence += 0.1
        if (factors.crowdDensity > 0) confidence += 0.1
        if (factors.areaRiskLevel > 0) confidence += 0.1
        if (factors.historicalIncidents > 0) confidence += 0.1
        if (factors.isNightTime) confidence += 0.1
        
        return confidence.coerceIn(0.0, 1.0)
    }
    
    private fun calculateDistance(location1: LocationData, location2: LocationData): Double {
        val lat1 = Math.toRadians(location1.location.latitude)
        val lon1 = Math.toRadians(location1.location.longitude)
        val lat2 = Math.toRadians(location2.location.latitude)
        val lon2 = Math.toRadians(location2.location.longitude)
        
        val dlat = lat2 - lat1
        val dlon = lon2 - lon1
        
        val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
        val c = 2 * asin(sqrt(a))
        
        return 6371000 * c // Earth's radius in meters
    }
    
    private fun calculateRouteDeviation(locations: List<LocationData>): Double {
        // Simple route deviation calculation
        // In production, this would use more sophisticated algorithms
        if (locations.size < 3) return 0.0
        
        val start = locations.first()
        val end = locations.last()
        val middle = locations[locations.size / 2]
        
        // Calculate expected position and compare with actual
        val expectedLat = (start.location.latitude + end.location.latitude) / 2
        val expectedLon = (start.location.longitude + end.location.longitude) / 2
        
        val deviation = calculateDistance(
            LocationData(Location("").apply { latitude = expectedLat; longitude = expectedLon }, LocalDateTime.now()),
            middle
        )
        
        return deviation
    }
}

data class LocationData(
    val location: Location,
    val timestamp: LocalDateTime
)

data class BehaviorPattern(
    val userId: String,
    val averageSpeed: Double,
    val commonRoutes: List<String>,
    val typicalCheckInFrequency: Long, // hours
    val communicationPattern: CommunicationPattern
)

data class CommunicationPattern(
    val averageResponseTime: Long, // minutes
    val typicalCommunicationFrequency: Long, // hours
    val preferredCommunicationMethods: List<String>
)

data class RiskFactor(
    val factorType: RiskFactorType,
    val value: Double,
    val timestamp: LocalDateTime
)

enum class RiskFactorType {
    WEATHER, CROWD_DENSITY, TIME_OF_DAY, SOLO_TRAVEL, AREA_RISK, HISTORICAL_INCIDENTS
}

data class AnomalyResult(
    val anomalies: List<Anomaly>,
    val severity: AnomalySeverity,
    val confidence: Double,
    val timestamp: LocalDateTime
)

data class Anomaly(
    val type: AnomalyType,
    val severity: AnomalySeverity,
    val description: String,
    val location: Location?,
    val timestamp: LocalDateTime,
    val confidence: Double
)

enum class AnomalyType {
    SPEED_ANOMALY, ROUTE_DEVIATION, LOCATION_DROPOFF, PROLONGED_INACTIVITY,
    COMMUNICATION_SILENCE, APP_USAGE_ANOMALY, MISSED_CHECKIN
}

enum class AnomalySeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}

// Data classes moved to RiskPredictor.kt to avoid duplication

enum class RiskLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

data class UserBehavior(
    val userId: String,
    val lastCommunicationTime: LocalDateTime,
    val lastAppUsageTime: LocalDateTime,
    val lastCheckInTime: LocalDateTime,
    val averageAppUsageDuration: Long, // minutes
    val communicationFrequency: Long // hours
)

// Local data classes to avoid import issues
data class TimeWindow(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

data class RiskPrediction(
    val location: android.location.Location,
    val timeWindow: TimeWindow,
    val riskScore: Double,
    val riskLevel: RiskLevel,
    val factors: RiskFactors,
    val recommendations: List<String>,
    val confidence: Double,
    val timestamp: LocalDateTime
)

data class RiskFactors(
    val weatherRisk: Double,
    val crowdDensity: Double,
    val isNightTime: Boolean,
    val isSoloTravel: Boolean,
    val areaRiskLevel: Double,
    val historicalIncidents: Double
)
