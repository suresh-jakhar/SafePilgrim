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
import androidx.compose.ui.unit.dp
import com.example.sp.blockchain.DigitalTouristID
import com.example.sp.blockchain.TouristStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalIDVerificationScreen(
    digitalID: DigitalTouristID,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showQRCode by remember { mutableStateOf(false) }
    
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
                text = "Digital Tourist ID",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        // ID Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (getTouristStatus(digitalID)) {
                    TouristStatus.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
                    TouristStatus.EXPIRED -> MaterialTheme.colorScheme.errorContainer
                    TouristStatus.NOT_STARTED -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = when (getTouristStatus(digitalID)) {
                            TouristStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                            TouristStatus.EXPIRED -> MaterialTheme.colorScheme.error
                            TouristStatus.NOT_STARTED -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Text(
                        text = "Status: ${getTouristStatus(digitalID).name}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Text(
                    text = "ID: ${digitalID.id}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = "Blockchain Hash: ${digitalID.blockchainHash}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        
        // Personal Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.titleMedium
                )
                
                InfoRow("Name", digitalID.name, Icons.Default.Person)
                InfoRow("Nationality", digitalID.nationality, Icons.Default.Flag)
                InfoRow("Passport", "***${digitalID.passportNumber.takeLast(4)}", Icons.Default.Badge)
            }
        }
        
        // Travel Dates
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Travel Dates",
                    style = MaterialTheme.typography.titleMedium
                )
                
                InfoRow(
                    "Entry Date",
                    digitalID.entryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    Icons.Default.DateRange
                )
                
                InfoRow(
                    "Exit Date",
                    digitalID.exitDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    Icons.Default.DateRange
                )
                
                InfoRow(
                    "Duration",
                    "${java.time.Duration.between(digitalID.entryDate, digitalID.exitDate).toDays()} days",
                    Icons.Default.Schedule
                )
            }
        }
        
        // Emergency Contacts
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Emergency Contacts",
                    style = MaterialTheme.typography.titleMedium
                )
                
                digitalID.emergencyContacts.forEach { contact ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        InfoRow("Name", contact.name, Icons.Default.Person)
                        InfoRow("Relationship", contact.relationship, Icons.Default.FamilyRestroom)
                        InfoRow("Phone", contact.phoneNumber, Icons.Default.Phone)
                        InfoRow("Email", contact.email, Icons.Default.Email)
                        if (contact != digitalID.emergencyContacts.last()) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
        
        // Itinerary
        if (digitalID.itinerary.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Travel Itinerary",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    digitalID.itinerary.forEach { item ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            InfoRow("Location", item.location, Icons.Default.LocationOn)
                            InfoRow(
                                "Planned Date",
                                item.plannedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                                Icons.Default.DateRange
                            )
                            InfoRow("Status", item.status.name, Icons.Default.Info)
                            if (item.actualDate != null) {
                                InfoRow(
                                    "Actual Date",
                                    item.actualDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                                    Icons.Default.CheckCircle
                                )
                            }
                            if (item != digitalID.itinerary.last()) {
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        }
        
        // QR Code Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "QR Code",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Button(
                    onClick = { showQRCode = !showQRCode },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        if (showQRCode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (showQRCode) "Hide QR Code" else "Show QR Code")
                }
                
                if (showQRCode) {
                    // In production, this would show an actual QR code
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                modifier = Modifier.size(200.dp)
                            )
                            Text(
                                text = digitalID.qrCode,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }
            
            Button(
                onClick = {
                    // In production, this would share the digital ID
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share")
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun getTouristStatus(digitalID: DigitalTouristID): TouristStatus {
    val now = LocalDateTime.now()
    return when {
        !digitalID.isActive -> TouristStatus.INACTIVE
        now.isAfter(digitalID.exitDate) -> TouristStatus.EXPIRED
        now.isBefore(digitalID.entryDate) -> TouristStatus.NOT_STARTED
        else -> TouristStatus.ACTIVE
    }
}
