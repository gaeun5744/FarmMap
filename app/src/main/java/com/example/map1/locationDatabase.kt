package com.example.map1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [locationArray::class], version = 2, exportSchema = false)
abstract class locationDatabase: RoomDatabase() {
    abstract fun locationArrayDao(): locationArrayDao

    companion object {
        private var instance: locationDatabase? = null

        @Synchronized
        fun getInstance(context: Context): locationDatabase? {
            if (instance == null) {
                synchronized(locationDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        locationDatabase::class.java,
                        "locationDatabase"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }

}