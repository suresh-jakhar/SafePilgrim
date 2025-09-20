package com.example.sp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sp.blockchain.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalIDRegistrationScreen(
    onRegistrationComplete: (DigitalTouristID) -> Unit,
    onBack: () -> Unit
) {
    var passportNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var entryDate by remember { mutableStateOf("") }
    var exitDate by remember { mutableStateOf("") }
    var emergencyContactName by remember { mutableStateOf("") }
    var emergencyContactPhone by remember { mutableStateOf("") }
    var emergencyContactEmail by remember { mutableStateOf("") }
    var emergencyContactRelationship by remember { mutableStateOf("") }
    var itineraryLocation by remember { mutableStateOf("") }
    var itineraryDate by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var registeredID by remember { mutableStateOf<DigitalTouristID?>(null) }
    
    val scrollState = rememberScrollState()
    
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
                text = "Digital Tourist ID Registration",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        // Personal Information Section
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
                
                OutlinedTextField(
                    value = passportNumber,
                    onValueChange = { passportNumber = it },
                    label = { Text("Passport Number") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = nationality,
                    onValueChange = { nationality = it },
                    label = { Text("Nationality") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) }
                )
            }
        }
        
        // Travel Dates Section
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
                
                OutlinedTextField(
                    value = entryDate,
                    onValueChange = { entryDate = it },
                    label = { Text("Entry Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                OutlinedTextField(
                    value = exitDate,
                    onValueChange = { exitDate = it },
                    label = { Text("Exit Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        
        // Emergency Contact Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Emergency Contact",
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = emergencyContactName,
                    onValueChange = { emergencyContactName = it },
                    label = { Text("Contact Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = emergencyContactRelationship,
                    onValueChange = { emergencyContactRelationship = it },
                    label = { Text("Relationship") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.FamilyRestroom, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = emergencyContactPhone,
                    onValueChange = { emergencyContactPhone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                
                OutlinedTextField(
                    value = emergencyContactEmail,
                    onValueChange = { emergencyContactEmail = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }
        }
        
        // Itinerary Section
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
                
                OutlinedTextField(
                    value = itineraryLocation,
                    onValueChange = { itineraryLocation = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = itineraryDate,
                    onValueChange = { itineraryDate = it },
                    label = { Text("Planned Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        
        // Error Message
        if (errorMessage.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        // Register Button
        Button(
            onClick = {
                if (validateInputs(
                    passportNumber, name, nationality, entryDate, exitDate,
                    emergencyContactName, emergencyContactPhone, emergencyContactEmail
                )) {
                    isLoading = true
                    errorMessage = ""
                    
                    // Simulate registration process
                    // In production, this would call the actual registration service
                    try {
                        val registrationData = TouristRegistrationData(
                            passportNumber = passportNumber,
                            name = name,
                            nationality = nationality,
                            entryDate = LocalDateTime.parse("${entryDate}T00:00:00"),
                            exitDate = LocalDateTime.parse("${exitDate}T23:59:59"),
                            emergencyContacts = listOf(
                                EmergencyContact(
                                    name = emergencyContactName,
                                    relationship = emergencyContactRelationship,
                                    phoneNumber = emergencyContactPhone,
                                    email = emergencyContactEmail
                                )
                            ),
                            itinerary = if (itineraryLocation.isNotEmpty()) {
                                listOf(
                                    ItineraryItem(
                                        id = "itinerary_1",
                                        location = itineraryLocation,
                                        plannedDate = LocalDateTime.parse("${itineraryDate}T12:00:00")
                                    )
                                )
                            } else {
                                emptyList()
                            }
                        )
                        
                        // For demo purposes, create a mock digital ID
                        val mockDigitalID = DigitalTouristID(
                            id = "SP_${System.currentTimeMillis()}_${(1000..9999).random()}",
                            passportNumber = passportNumber,
                            name = name,
                            nationality = nationality,
                            entryDate = LocalDateTime.parse("${entryDate}T00:00:00"),
                            exitDate = LocalDateTime.parse("${exitDate}T23:59:59"),
                            emergencyContacts = listOf(
                                EmergencyContact(
                                    name = emergencyContactName,
                                    relationship = emergencyContactRelationship,
                                    phoneNumber = emergencyContactPhone,
                                    email = emergencyContactEmail
                                )
                            ),
                            itinerary = if (itineraryLocation.isNotEmpty()) {
                                listOf(
                                    ItineraryItem(
                                        id = "itinerary_1",
                                        location = itineraryLocation,
                                        plannedDate = LocalDateTime.parse("${itineraryDate}T12:00:00")
                                    )
                                )
                            } else {
                                emptyList()
                            },
                            qrCode = "QR_CODE_${System.currentTimeMillis()}",
                            blockchainHash = "0x${generateRandomHash()}"
                        )
                        
                        registeredID = mockDigitalID
                        showSuccessDialog = true
                        isLoading = false
                    } catch (e: Exception) {
                        errorMessage = "Registration failed: ${e.message}"
                        isLoading = false
                    }
                } else {
                    errorMessage = "Please fill in all required fields"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isLoading) "Registering..." else "Register Digital ID")
        }
    }
    
    // Success Dialog
    if (showSuccessDialog && registeredID != null) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Registration Successful!") },
            text = { 
                Column {
                    Text("Your Digital Tourist ID has been created:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("ID: ${registeredID!!.id}", style = MaterialTheme.typography.bodySmall)
                    Text("QR Code: ${registeredID!!.qrCode}", style = MaterialTheme.typography.bodySmall)
                    Text("Blockchain Hash: ${registeredID!!.blockchainHash}", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onRegistrationComplete(registeredID!!)
                    }
                ) {
                    Text("Continue")
                }
            }
        )
    }
}

private fun validateInputs(
    passportNumber: String,
    name: String,
    nationality: String,
    entryDate: String,
    exitDate: String,
    emergencyContactName: String,
    emergencyContactPhone: String,
    emergencyContactEmail: String
): Boolean {
    return passportNumber.isNotEmpty() &&
            name.isNotEmpty() &&
            nationality.isNotEmpty() &&
            entryDate.isNotEmpty() &&
            exitDate.isNotEmpty() &&
            emergencyContactName.isNotEmpty() &&
            emergencyContactPhone.isNotEmpty() &&
            emergencyContactEmail.isNotEmpty()
}

private fun generateRandomHash(): String {
    val chars = "0123456789abcdef"
    return (1..64).map { chars.random() }.joinToString("")
}
