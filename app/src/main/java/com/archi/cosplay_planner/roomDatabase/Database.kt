package com.archi.cosplay_planner.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Costume::class, Events::class, Detail::class, Materials::class, MaterialsPlanned::class, CosplayPhoto::class],version = 2)
@TypeConverters(Converters::class)
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
                ).addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }


    }


}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
                    CREATE TABLE materials_planned_temp (
                        materialPlannedID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        materialID INTEGER DEFAULT 0,
                        quantity REAL DEFAULT 0,
                        detailID INTEGER DEFAULT 0
                    )
                """)
        db.execSQL("""
                    INSERT INTO materials_planned_temp (materialPlannedID, materialID, quantity, detailID)
                    SELECT materialPlannedID, materialID, quantity, detailID FROM MaterialsPlanned
                """)
        db.execSQL("DROP TABLE MaterialsPlanned")
        db.execSQL("ALTER TABLE materials_planned_temp RENAME TO MaterialsPlanned")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
                    CREATE TABLE materials_planned_temp (
                        materialPlannedID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        materialID INTEGER DEFAULT 0,
                        quantity REAL DEFAULT 0,
                        detailID INTEGER DEFAULT 0
                    )
                """)
        db.execSQL("""
                    INSERT INTO materials_planned_temp (materialPlannedID, materialID, quantity, detailID)
                    SELECT materialPlannedID, materialID, quantity, detailID FROM MaterialsPlanned
                """)
        db.execSQL("DROP TABLE MaterialsPlanned")
        db.execSQL("ALTER TABLE materials_planned_temp RENAME TO MaterialsPlanned")
    }
}