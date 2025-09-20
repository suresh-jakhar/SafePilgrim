package com.example.sp.geofence

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow

@Composable
fun GeoFenceScreen(
    geofenceManager: GeofenceManager,
    locationFlow: Flow<Location>,
    onBack: () -> Unit
) {
    val radius = remember { mutableIntStateOf(200) }
    val fenceId = remember { mutableStateOf("home_fence") }
    val loc by locationFlow.collectAsState(initial = null)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = fenceId.value, onValueChange = { fenceId.value = it }, label = { Text("Fence ID") })
        OutlinedTextField(value = radius.intValue.toString(), onValueChange = { it.toIntOrNull()?.let { v -> radius.intValue = v } }, label = { Text("Radius (m)") })
        Button(enabled = loc != null, onClick = {
            val l = loc ?: return@Button
            geofenceManager.addGeofence(fenceId.value, l.latitude, l.longitude, radius.intValue.toFloat())
        }) { Text("Add at Current Location") }
        Button(onClick = { geofenceManager.removeGeofence(fenceId.value) }) { Text("Remove Fence") }
        Button(onClick = onBack) { Text("Back") }
    }
}


