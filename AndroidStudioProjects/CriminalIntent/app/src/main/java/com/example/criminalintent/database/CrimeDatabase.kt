package com.example.criminalintent.database


import com.example.criminalintent.Crime
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Database(entities = [Crime::class], version=1)
@TypeConverters (CrimeTypeConverters:: class)
abstract class CrimeDatabase: RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}
class CrimeTypeConverters {
    @TypeConverter
    fun fromDate (date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun toDate (millisSinceEpoch: Long) : Date {
        return Date (millisSinceEpoch)
    }
}