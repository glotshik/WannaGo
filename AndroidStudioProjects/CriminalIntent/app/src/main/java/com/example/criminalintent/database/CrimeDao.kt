package com.example.criminalintent.database

import com.example.criminalintent.Crime
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): Flow<List<Crime>>

    @Query ("SELECT * FROM crime WHERE id =(:id)")
    suspend fun getCrime(id:UUID): Crime
    @Update
    suspend fun updateCrime (crime: Crime)

}