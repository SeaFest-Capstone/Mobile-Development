package com.example.seafest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FishEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FishDatabase : RoomDatabase(){

    abstract fun fishDao(): FishDao


    companion object {
        @Volatile
        private var INSTANCE: FishDatabase? = null

        fun getInstance(context: Context): FishDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FishDatabase::class.java,
                    "Fish.db"
                ).build()
            }
    }
}
