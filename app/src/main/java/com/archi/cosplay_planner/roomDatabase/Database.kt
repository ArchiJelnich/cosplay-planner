package com.archi.cosplay_planner.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Costume::class, Events::class, Detail::class, Materials::class, MaterialsPlanned::class, CosplayPhoto::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CostumeDao(): CostumeDao
    abstract fun EventsDao(): EventsDao
    abstract fun DetailDao(): DetailDao
    abstract fun MaterialsDao(): MaterialsDao
    abstract fun MaterialsPlannedDao(): MaterialsPlannedDao
    abstract fun PhotoDAO(): PhotoDAO


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cosplay_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }


}