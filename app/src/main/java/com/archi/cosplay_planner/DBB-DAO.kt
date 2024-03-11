package com.archi.cosplay_planner

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CostumeDao {
    @Query("SELECT * FROM Costume")
    fun getAll():  List<Costume>
    @Insert
    fun insertAll(vararg users: Costume)
    @Delete
    fun delete(costume: Costume)
}

@Dao
interface EventsDao {
    @Query("SELECT * FROM Events")
    fun getAll(): List<Events>
    @Insert
    fun insertAll(vararg event: Events)
    @Delete
    fun delete(event: Events)
}

@Dao
interface DetailDao {
    @Query("SELECT * FROM Detail")
    fun getAll(): List<Detail>
    @Insert
    fun insertAll(vararg users: Detail)
    @Delete
    fun delete(detail: Detail)
}

@Dao
interface MaterialsDao {
    @Query("SELECT * FROM Materials")
    fun getAll(): List<Materials>
    @Insert
    fun insertAll(vararg users: Materials)
    @Delete
    fun delete(materials: Materials)
}

@Dao
interface MaterialsStockDao {
    @Query("SELECT * FROM MaterialsStock")
    fun getAll(): List<MaterialsStock>
    @Insert
    fun insertAll(vararg users: MaterialsStock)
    @Delete
    fun delete(materialsStock: MaterialsStock)
}

@Dao
interface MaterialsPlannedDao {
    @Query("SELECT * FROM MaterialsPlanned")
    fun getAll(): List<MaterialsPlanned>
    @Insert
    fun insertAll(vararg users: MaterialsPlanned)
    @Delete
    fun delete(materialsPlanned: MaterialsPlanned)
}