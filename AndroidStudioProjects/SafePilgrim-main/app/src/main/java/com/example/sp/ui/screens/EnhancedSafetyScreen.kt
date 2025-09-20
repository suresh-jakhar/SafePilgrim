package com.example.sp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sp.ai.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSafetyScreen(
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    // State variables
    var areaRisk by remember { mutableStateOf(3) }
    var isNight by remember { mutableStateOf(false) }
    var isSolo by remember { mutableStateOf(true) }
    var incidentHistory by remember { mutableStateOf(20) }
    var weatherRisk by remember { mutableStateOf(10) }
    
    var enhancedScore by remember { mutableStateOf<EnhancedSafetyScore?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showPredictions by remember { mutableStateOf(false) }
    
    // AI components
    val context = androidx.compose.ui.platform.LocalContext.current
    val enhancedCalculator = remember { EnhancedSafetyCalculator() }
    val anomalyDetector = remember { AnomalyDetector(context) }
    val riskPredictor = remember { RiskPredictor(context) }
    
    // Calculate enhanced safety score
    fun calculateEnhancedScore() {
        scope.launch {
            isLoading = true
            try {
                val inputs = SafetyInputs(
                    areaRiskLevel = areaRisk,
                    isNightTime = isNight,
                    isSoloTravel = isSolo,
                    incidentHistoryScore = incidentHistory,
                    weatherRiskScore = weatherRisk
                )
                
                val realTimeData = RealTimeData(
                    crowdDensity = 65.0,
                    weatherCondition = WeatherCondition.CLEAR,
                    trafficCondition = TrafficCondition.MODERATE,
                    publicTransportStatus = PublicTransportStatus.NORMAL,
                    emergencyServicesStatus = EmergencyServicesStatus.NORMAL,
                    hasLocalEvents = false,
                    politicalSituation = PoliticalSituation.STABLE
                )
                
                val userBehavior = UserBehavior(
                    userId = "user_123",
                    lastCommunicationTime = LocalDateTime.now().minusHours(2),
                    lastAppUsageTime = LocalDateTime.now().minusMinutes(30),
                    lastCheckInTime = LocalDateTime.now().minusHours(6),
                    averageAppUsageDuration = 15,
                    communicationFrequency = 4
                )
                
                enhancedScore = enhancedCalculator.calculateWithAI(inputs, realTimeData, userBehavior)
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }
    
    // Calculate score on first load
    LaunchedEffect(Unit) {
        calculateEnhancedScore()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "AI-Enhanced Safety Score",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        // Main Safety Score Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = enhancedScore?.color ?: MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = enhancedScore?.color ?: MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Safety Score: ${enhancedScore?.score?.toInt() ?: 0}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                
                Text(
                    text = "Level: ${enhancedScore?.label ?: "Calculating..."}",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = "Confidence: ${(enhancedScore?.confidence ?: 0.0 * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        
        // Score Breakdown
        if (enhancedScore != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Score Breakdown",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    ScoreBreakdownRow("Base Score", enhancedScore!!.baseScore)
                    ScoreBreakdownRow("AI Adjustments", enhancedScore!!.aiAdjustments)
                    ScoreBreakdownRow("Behavior Score", enhancedScore!!.behaviorScore)
                    ScoreBreakdownRow("Real-time Risk", enhancedScore!!.realTimeRisk)
                }
            }
        }
        
        // Input Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Safety Factors",
                    style = MaterialTheme.typography.titleMedium
                )
                
                // Area Risk Level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Area Risk Level")
                    Slider(
                        value = areaRisk.toFloat(),
                        onValueChange = { areaRisk = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 3,
                        modifier = Modifier.width(200.dp)
                    )
                    Text("$areaRisk")
                }
                
                // Night Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Night Time")
                    Switch(
                        checked = isNight,
                        onCheckedChange = { isNight = it }
                    )
                }
                
                // Solo Travel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solo Travel")
                    Switch(
                        checked = isSolo,
                        onCheckedChange = { isSolo = it }
                    )
                }
                
                // Incident History
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Incident History")
                    Slider(
                        value = incidentHistory.toFloat(),
                        onValueChange = { incidentHistory = it.toInt() },
                        valueRange = 0f..100f,
                        steps = 9,
                        modifier = Modifier.width(200.dp)
                    )
                    Text("$incidentHistory")
                }
                
                // Weather Risk
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Weather Risk")
                    Slider(
                        value = weatherRisk.toFloat(),
                        onValueChange = { weatherRisk = it.toInt() },
                        valueRange = 0f..100f,
                        steps = 9,
                        modifier = Modifier.width(200.dp)
                    )
                    Text("$weatherRisk")
                }
                
                Button(
                    onClick = { calculateEnhancedScore() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Recalculate Score")
                }
            }
        }
        
        // AI Recommendations
        if (enhancedScore != null && enhancedScore!!.recommendations.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "AI Recommendations",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    enhancedScore!!.recommendations.forEach { recommendation ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = recommendation,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        
        // Risk Predictions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Risk Predictions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = showPredictions,
                        onCheckedChange = { showPredictions = it }
                    )
                }
                
                if (showPredictions) {
                    Text(
                        text = "AI-powered risk predictions for your planned route and time",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Button(
                        onClick = {
                            // In production, this would show actual risk predictions
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Route Risk Analysis")
                    }
                }
            }
        }
        
        // Back Button
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun ScoreBreakdownRow(
    label: String,
    value: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${value.toInt()}",
            style = MaterialTheme.typography.bodyMedium,
            color = when {
                value > 0 -> Color(0xFF2E7D32)
                value < 0 -> Color(0xFFC62828)
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
