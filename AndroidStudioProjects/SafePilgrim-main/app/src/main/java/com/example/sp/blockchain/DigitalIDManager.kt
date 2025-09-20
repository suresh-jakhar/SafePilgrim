package com.example.sp.blockchain

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@Dao
interface DigitalIDDao {
    @Query("SELECT * FROM digital_tourist_ids WHERE id = :id")
    suspend fun getDigitalID(id: String): DigitalTouristID?
    
    @Query("SELECT * FROM digital_tourist_ids WHERE isActive = 1")
    fun getAllActiveIDs(): Flow<List<DigitalTouristID>>
    
    @Query("SELECT * FROM digital_tourist_ids WHERE passportNumber = :passportNumber")
    suspend fun getDigitalIDByPassport(passportNumber: String): DigitalTouristID?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDigitalID(digitalID: DigitalTouristID)
    
    @Update
    suspend fun updateDigitalID(digitalID: DigitalTouristID)
    
    @Query("UPDATE digital_tourist_ids SET isActive = 0 WHERE id = :id")
    suspend fun deactivateDigitalID(id: String)
    
    @Query("DELETE FROM digital_tourist_ids WHERE id = :id")
    suspend fun deleteDigitalID(id: String)
}

@Database(
    entities = [DigitalTouristID::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SafePilgrimDatabase : RoomDatabase() {
    abstract fun digitalIDDao(): DigitalIDDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, java.time.ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(java.time.ZoneOffset.UTC)?.toEpochSecond()
    }
    
    @TypeConverter
    fun fromEmergencyContactList(value: List<EmergencyContact>?): String? {
        return if (value == null) null else {
            val gson = com.google.gson.Gson()
            gson.toJson(value)
        }
    }
    
    @TypeConverter
    fun toEmergencyContactList(value: String?): List<EmergencyContact>? {
        return if (value == null) null else {
            val gson = com.google.gson.Gson()
            val listType = object : com.google.gson.reflect.TypeToken<List<EmergencyContact>>() {}.type
            gson.fromJson(value, listType)
        }
    }
    
    @TypeConverter
    fun fromItineraryItemList(value: List<ItineraryItem>?): String? {
        return if (value == null) null else {
            val gson = com.google.gson.Gson()
            gson.toJson(value)
        }
    }
    
    @TypeConverter
    fun toItineraryItemList(value: String?): List<ItineraryItem>? {
        return if (value == null) null else {
            val gson = com.google.gson.Gson()
            val listType = object : com.google.gson.reflect.TypeToken<List<ItineraryItem>>() {}.type
            gson.fromJson(value, listType)
        }
    }
}

class DigitalIDManager(private val context: Context) {
    private val database: SafePilgrimDatabase by lazy {
        Room.databaseBuilder(
            context,
            SafePilgrimDatabase::class.java,
            "safepilgrim_database"
        ).build()
    }
    
    private val dao: DigitalIDDao by lazy { database.digitalIDDao() }
    private val blockchainService: BlockchainService by lazy { BlockchainService(context) }
    
    suspend fun registerTourist(registrationData: TouristRegistrationData): DigitalTouristID {
        // Check if tourist already exists
        val existingID = dao.getDigitalIDByPassport(registrationData.passportNumber)
        if (existingID != null && existingID.isActive) {
            throw IllegalStateException("Tourist already registered with this passport")
        }
        
        // Generate digital ID
        val digitalID = blockchainService.generateDigitalID(registrationData)
        
        // Store in local database
        dao.insertDigitalID(digitalID)
        
        return digitalID
    }
    
    suspend fun getDigitalID(id: String): DigitalTouristID? {
        return dao.getDigitalID(id)
    }
    
    suspend fun getDigitalIDByPassport(passportNumber: String): DigitalTouristID? {
        return dao.getDigitalIDByPassport(passportNumber)
    }
    
    fun getAllActiveIDs(): Flow<List<DigitalTouristID>> {
        return dao.getAllActiveIDs()
    }
    
    suspend fun updateLocation(touristId: String, location: LocationData) {
        val digitalID = dao.getDigitalID(touristId)
        if (digitalID != null) {
            blockchainService.updateLocation(touristId, location, System.currentTimeMillis())
            
            // Update local record
            val updatedID = digitalID.copy(updatedAt = LocalDateTime.now())
            dao.updateDigitalID(updatedID)
        }
    }
    
    suspend fun recordIncident(incident: IncidentRecord) {
        blockchainService.recordIncident(incident)
    }
    
    suspend fun verifyQRCode(qrCode: String): Boolean {
        return blockchainService.verifyID(qrCode)
    }
    
    suspend fun deactivateTourist(id: String) {
        dao.deactivateDigitalID(id)
    }
    
    suspend fun deleteTourist(id: String) {
        dao.deleteDigitalID(id)
    }
    
    suspend fun getTouristStatus(id: String): TouristStatus {
        val digitalID = dao.getDigitalID(id) ?: return TouristStatus.NOT_FOUND
        
        return when {
            !digitalID.isActive -> TouristStatus.INACTIVE
            LocalDateTime.now().isAfter(digitalID.exitDate) -> TouristStatus.EXPIRED
            LocalDateTime.now().isBefore(digitalID.entryDate) -> TouristStatus.NOT_STARTED
            else -> TouristStatus.ACTIVE
        }
    }
    
    suspend fun getTouristItinerary(id: String): List<ItineraryItem> {
        val digitalID = dao.getDigitalID(id)
        return digitalID?.itinerary ?: emptyList()
    }
    
    suspend fun updateItineraryStatus(id: String, itineraryItemId: String, status: ItineraryStatus) {
        val digitalID = dao.getDigitalID(id) ?: return
        
        val updatedItinerary = digitalID.itinerary.map { item ->
            if (item.id == itineraryItemId) {
                item.copy(
                    status = status,
                    actualDate = if (status == ItineraryStatus.COMPLETED) LocalDateTime.now() else item.actualDate
                )
            } else {
                item
            }
        }
        
        val updatedDigitalID = digitalID.copy(
            itinerary = updatedItinerary,
            updatedAt = LocalDateTime.now()
        )
        
        dao.updateDigitalID(updatedDigitalID)
    }
}

enum class TouristStatus {
    ACTIVE, INACTIVE, EXPIRED, NOT_STARTED, NOT_FOUND
}
