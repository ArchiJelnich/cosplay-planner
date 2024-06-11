package com.archi.cosplay_planner.P_ROOM

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CostumeDao {
    @Query("SELECT * FROM Costume")
    fun getAll():  List<Costume>

    @Query("SELECT costumeID FROM Costume WHERE character = :character")
    fun getByCharacter(character: String):  List<Int>

    @Query("SELECT * FROM Costume WHERE costumeID = :id")
    fun getByID(id: Int):  Costume

    @Query("SELECT costumeID FROM Costume WHERE status = 0")
    fun getIDByInProgress():  List<Int>

    @Query("SELECT * FROM Costume WHERE status = 0")
    fun getP():  List<Costume>

    @Query("SELECT * FROM Costume WHERE status = 1")
    fun getF():  List<Costume>

    @Query("SELECT * FROM Costume WHERE status = 2")
    fun getH():  List<Costume>

    @Insert
    fun insertAll(vararg users: Costume)
    @Delete
    fun delete(costume: Costume)

    @Update
    fun updateCostume(costume: Costume)
}

@Dao
interface EventsDao {
    @Query("SELECT * FROM Events")
    fun getAll(): List<Events>
    @Insert
    fun insertAll(vararg event: Events)
    @Delete
    fun delete(event: Events)
    @Update
    fun updateEvent(event: Events)
    @Query("SELECT * FROM Events WHERE costumeID = :costumeID")
    fun getByCostume(costumeID: Int):  List<Events>

    @Query("UPDATE Events SET costumeID = -1 WHERE costumeID = :costumeID")
    fun updateWhenDelete(costumeID: Int)
    @Query("DELETE FROM Events WHERE costumeID = :costumeID")
    fun deleteByCostumeID(costumeID: Int)

}

@Dao
interface DetailDao {
    @Query("SELECT * FROM Detail")
    fun getAll(): List<Detail>
    @Insert
    fun insertAll(vararg users: Detail)
    @Delete
    fun delete(detail: Detail)
    @Query("SELECT * FROM Detail WHERE costumeID = :costumeID")
    fun getByCostume(costumeID: Int):  List<Detail>
    @Query("SELECT * FROM Detail WHERE detailID = :detailID")
    fun getByID(detailID: Int):  List<Detail>
    @Query("SELECT detailID FROM Detail WHERE costumeID = :costumeID")
    fun getIDByCostume(costumeID: Int):  List<Int>
    @Query("DELETE FROM Detail WHERE costumeID = :costumeID")
    fun deleteByCostumeID(costumeID: Int)
    @Update
    fun updateDetail(detail: Detail)
}

@Dao
interface MaterialsDao {
    @Query("SELECT * FROM Materials")
    fun getAll(): List<Materials>
    @Query("SELECT * FROM Materials WHERE material LIKE :filter")
    fun getFiltered(filter : String): List<Materials>
    @Insert
    fun insertAll(vararg users: Materials)
    @Delete
    fun delete(materials: Materials)

    @Query("SELECT materialID FROM Materials WHERE material = :material")
    fun getByName(material: String):  List<Int>

    @Query("SELECT material FROM Materials WHERE materialID = :materialID")
    fun getNameByID(materialID: Int):  List<String>

    @Query("SELECT unit FROM Materials WHERE materialID = :materialID")
    fun getUnitByID(materialID: Int):  List<String>

    @Query("SELECT materialID FROM Materials WHERE material = :material")
    fun getIDbyName(material: String):  List<Int>

    @Update
    fun updateMaterial(materials: Materials)
}

@Dao
interface MaterialsPlannedDao {
    @Query("SELECT * FROM MaterialsPlanned")
    fun getAll(): List<MaterialsPlanned>
    @Insert
    fun insertAll(vararg materialsp: MaterialsPlanned)
    @Query("DELETE FROM MaterialsPlanned")
    fun delete()
    @Query("SELECT * FROM MaterialsPlanned WHERE detailID = :detailID")
    fun getByDetail(detailID: Int):  List<MaterialsPlanned>
    @Query("DELETE FROM MaterialsPlanned WHERE detailID = :detailID")
    fun deleteByDetail(detailID: Int)
    @Query("SELECT materialPlannedID FROM MaterialsPlanned WHERE materialID = :materialID")
    fun getByMaterial(materialID: Int): List<Int>

    @Query("SELECT * FROM MaterialsPlanned WHERE materialID = :materialID AND detailID = :detailID")
    fun getByMaterialandDetail(materialID: Int, detailID: Int): List<MaterialsPlanned>

    @Query("DELETE FROM MaterialsPlanned WHERE materialPlannedID = :materialPlannedID")
    fun deleteBymaterialIP(materialPlannedID: Int)
    @Update
    fun updateMaterialP(materials: MaterialsPlanned)
    @Query("SELECT materialID as M, quantity as Q FROM MaterialsPlanned WHERE detailID IN (:detailIDlist) GROUP BY materialID")
    fun getAllRes(detailIDlist: List<Int>): List<MaterialQuanPair>

}

@Dao
interface PhotoDAO {
    @Insert
    fun insertPhoto(vararg cosplayphoto: CosplayPhoto)

    @Query("SELECT * FROM CosplayPhoto WHERE costumeID = :cosplayID")
    fun getByID(cosplayID: Int):  List<CosplayPhoto>

    @Update
    fun update(CosplayPhoto: CosplayPhoto)

    @Query("DELETE FROM CosplayPhoto WHERE costumeID = :cosplayID")
    fun deleteByID(cosplayID: Int)

}
