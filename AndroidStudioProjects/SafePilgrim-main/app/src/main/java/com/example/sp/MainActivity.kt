package com.example.sp

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Fence
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.remember
import com.example.sp.ui.theme.SPTheme
import com.example.sp.location.LocationService
import com.example.sp.panic.PanicScreen
import com.example.sp.safety.SafetyScreen
import com.example.sp.geofence.GeoFenceScreen
import com.example.sp.geofence.GeofenceManager
import com.example.sp.ui.screens.DigitalIDRegistrationScreen
import com.example.sp.ui.screens.DigitalIDVerificationScreen
import com.example.sp.ui.screens.EnhancedSafetyScreen
import com.example.sp.blockchain.DigitalIDManager
import com.example.sp.blockchain.DigitalTouristID

class MainActivity : ComponentActivity() {
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }
    
    private lateinit var digitalIDManager: DigitalIDManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestRuntimePermissionsIfNeeded()
        
        // Initialize Digital ID Manager
        digitalIDManager = DigitalIDManager(this)
        setContent {
            SPTheme {
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry.value?.destination?.route ?: "home"

                val title = when (currentRoute) {
                    "home" -> "SafePilgrim"
                    "map" -> "Live Location"
                    "panic" -> "Panic"
                    "safety" -> "Safety Score"
                    "enhanced_safety" -> "AI Safety Score"
                    "geofence" -> "GeoFence"
                    "digital_id_registration" -> "Register Digital ID"
                    "digital_id_verification" -> "Digital ID"
                    else -> "SafePilgrim"
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text(title) }) },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentRoute == "home",
                                onClick = { if (currentRoute != "home") navController.navigate("home") },
                                label = { Text("Home") },
                                icon = { Icon(Icons.Default.Home, contentDescription = null) }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "map",
                                onClick = { if (currentRoute != "map") navController.navigate("map") },
                                label = { Text("Map") },
                                icon = { Icon(Icons.Default.Map, contentDescription = null) }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "panic",
                                onClick = { if (currentRoute != "panic") navController.navigate("panic") },
                                label = { Text("Panic") },
                                icon = { Icon(Icons.Default.Campaign, contentDescription = null) }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "safety",
                                onClick = { if (currentRoute != "safety") navController.navigate("safety") },
                                label = { Text("Safety") },
                                icon = { Icon(Icons.Default.Security, contentDescription = null) }
                            )
                            NavigationBarItem(
                                selected = currentRoute == "geofence",
                                onClick = { if (currentRoute != "geofence") navController.navigate("geofence") },
                                label = { Text("Fence") },
                                icon = { Icon(Icons.Default.Fence, contentDescription = null) }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                        HomeScreen(
                            onOpenMap = { navController.navigate("map") },
                            onOpenPanic = { navController.navigate("panic") },
                            onOpenSafety = { navController.navigate("safety") },
                            onOpenEnhancedSafety = { navController.navigate("enhanced_safety") },
                            onOpenGeofence = { navController.navigate("geofence") },
                            onOpenDigitalIDRegistration = { navController.navigate("digital_id_registration") }
                        )
                        }
                        composable("map") {
                            val app = application as SPApp
                            LiveLocationMapScreen(locationFlow = app.container.locationService.locationUpdates())
                        }
                        composable("panic") {
                            val app = application as SPApp
                            PanicScreen(
                                locationFlow = app.container.locationService.locationUpdates(),
                                onRecordingFinished = { file, location ->
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("safety") {
                            SafetyScreen(onBack = { navController.popBackStack() })
                        }
                        composable("enhanced_safety") {
                            EnhancedSafetyScreen(onBack = { navController.popBackStack() })
                        }
                        composable("geofence") {
                            val app = application as SPApp
                            val manager = remember { GeofenceManager(this@MainActivity) }
                            GeoFenceScreen(
                                geofenceManager = manager,
                                locationFlow = app.container.locationService.locationUpdates(),
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("digital_id_registration") {
                            DigitalIDRegistrationScreen(
                                onRegistrationComplete = { digitalID ->
                                    // Store the digital ID and navigate to verification
                                    navController.navigate("digital_id_verification/${digitalID.id}")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("digital_id_verification/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            // In production, this would fetch the digital ID from the database
                            // For demo purposes, we'll create a mock digital ID
                            val mockDigitalID = DigitalTouristID(
                                id = id,
                                passportNumber = "A1234567",
                                name = "John Doe",
                                nationality = "USA",
                                entryDate = java.time.LocalDateTime.now().minusDays(1),
                                exitDate = java.time.LocalDateTime.now().plusDays(7),
                                emergencyContacts = listOf(
                                    com.example.sp.blockchain.EmergencyContact(
                                        name = "Jane Doe",
                                        relationship = "Spouse",
                                        phoneNumber = "+1-555-0123",
                                        email = "jane.doe@email.com"
                                    )
                                ),
                                itinerary = listOf(
                                    com.example.sp.blockchain.ItineraryItem(
                                        id = "itinerary_1",
                                        location = "Taj Mahal, Agra",
                                        plannedDate = java.time.LocalDateTime.now().plusDays(2)
                                    )
                                ),
                                qrCode = "QR_CODE_${System.currentTimeMillis()}",
                                blockchainHash = "0x1234567890abcdef"
                            )
                            DigitalIDVerificationScreen(
                                digitalID = mockDigitalID,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestRuntimePermissionsIfNeeded() {
        val required = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            required.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        val toRequest = required.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (toRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(toRequest.toTypedArray())
        }
    }
}

@Composable
fun HomeScreen(
    onOpenMap: () -> Unit = {},
    onOpenPanic: () -> Unit = {},
    onOpenSafety: () -> Unit = {},
    onOpenEnhancedSafety: () -> Unit = {},
    onOpenGeofence: () -> Unit = {},
    onOpenDigitalIDRegistration: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("SafePilgrim", style = MaterialTheme.typography.headlineMedium)
        Text("Quick actions", style = MaterialTheme.typography.labelLarge)

        ElevatedCard(onClick = onOpenMap, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Text("Live Map", style = MaterialTheme.typography.titleMedium)
                }
                Text("See your current location on the map", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ElevatedCard(onClick = onOpenDigitalIDRegistration, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Badge, contentDescription = null)
                    Text("Digital Tourist ID", style = MaterialTheme.typography.titleMedium)
                }
                Text("Register for blockchain-based digital identity", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ElevatedCard(onClick = onOpenSafety, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null)
                    Text("Safety Score", style = MaterialTheme.typography.titleMedium)
                }
                Text("Get an at-a-glance safety assessment", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ElevatedCard(onClick = onOpenEnhancedSafety, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Psychology, contentDescription = null)
                    Text("AI Safety Analysis", style = MaterialTheme.typography.titleMedium)
                }
                Text("Advanced AI-powered safety predictions", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ElevatedCard(onClick = onOpenGeofence, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Fence, contentDescription = null)
                    Text("GeoFence", style = MaterialTheme.typography.titleMedium)
                }
                Text("Set up a safe zone around your location", style = MaterialTheme.typography.bodyMedium)
            }
        }

        ElevatedCard(
            onClick = onOpenPanic,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                    Text("Panic", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                }
                Text("Record an emergency message with location", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SPTheme {
        HomeScreen()
    }
}