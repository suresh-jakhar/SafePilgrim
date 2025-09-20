package com.safepilgrim.companion.controller

import com.safepilgrim.companion.dto.*
import com.safepilgrim.companion.service.MobileApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/mobile")
class MobileApiController(
    private val mobileApiService: MobileApiService
) {
    
    @PostMapping("/sync-offline-data")
    fun syncOfflineData(@RequestBody offlineData: OfflineDataSync): ResponseEntity<SyncResult> {
        return try {
            val result = mobileApiService.syncOfflineData(offlineData)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                SyncResult(
                    success = false,
                    syncedItems = 0,
                    failedItems = offlineData.data.size,
                    message = e.message ?: "Sync failed"
                )
            )
        }
    }
    
    @GetMapping("/config")
    fun getMobileConfig(): ResponseEntity<MobileConfig> {
        return try {
            val config = mobileApiService.getMobileConfig()
            ResponseEntity.ok(config)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                MobileConfig(
                    apiEndpoints = emptyMap(),
                    features = emptyMap(),
                    settings = emptyMap()
                )
            )
        }
    }
    
    @PostMapping("/device-registration")
    fun registerDevice(@RequestBody deviceInfo: DeviceRegistration): ResponseEntity<RegistrationResult> {
        return try {
            val result = mobileApiService.registerDevice(deviceInfo)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                RegistrationResult(
                    success = false,
                    userId = null,
                    message = e.message ?: "Registration failed"
                )
            )
        }
    }
    
    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP", "service" to "android-companion"))
    }
}
