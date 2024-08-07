package com.archi.cosplay_planner.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.math.BigDecimal


@Entity
data class Costume(
    @PrimaryKey(autoGenerate = true) val costumeID: Int,
    @ColumnInfo(name = "fandom", defaultValue = "Mystery") val fandom: String?,
    @ColumnInfo(name = "character", defaultValue = "Mystery") val character: String?,
    @ColumnInfo(name = "status", defaultValue = "0") val status: Int?,
    // 0 - In progress
    // 1 - Finished
    // 2 - On hold
    @ColumnInfo(name = "progress", defaultValue = "0") val progress: Int?,
) : Serializable

@Entity
data class Events(
    @PrimaryKey(autoGenerate = true) val eventID: Int,
    @ColumnInfo(name = "event", defaultValue = "Mystery") val event: String?,
    @ColumnInfo(name = "type", defaultValue = "0") val type: Int?,
    // 0 - Con
    // 1 - Party
    // 2 - Photoshoot
    @ColumnInfo(name = "place", defaultValue = "Mystery") val place: String?,
    @ColumnInfo(name = "date", defaultValue = "Mystery") val date: String?,
    @ColumnInfo(name = "dateSorted", defaultValue = "Mystery") val dateSorted: Int?,
    @ColumnInfo(name = "costumeID", defaultValue = "0") val costumeID: Int?,
) : Serializable
@Entity
data class Detail(
    @PrimaryKey(autoGenerate = true) val detailID: Int,
    @ColumnInfo(name = "detail", defaultValue = "Mystery") val detail: String?,
    @ColumnInfo(name = "type", defaultValue = "0") val type: Int?,
    // 0 - Shoes
    // 1 - Wig
    // 2 - Accessories
    // 3 - Props
    // 4 - Tops
    // 5 - Bottom
    // 6 - Outerwear
    // 7 - Other
    @ColumnInfo(name = "progress", defaultValue = "0") val progress: Int?,
    @ColumnInfo(name = "costumeID", defaultValue = "0") val costumeID: Int?,
) : Serializable

@Entity
data class Materials(
    @PrimaryKey(autoGenerate = true) val materialID: Int,
    @ColumnInfo(name = "material", defaultValue = "Mystery") val material: String?,
    @ColumnInfo(name = "unit", defaultValue = "Mystery") val unit: String?,
) : Serializable

@Entity
data class MaterialsPlanned(
    @PrimaryKey(autoGenerate = true) val materialPlannedID: Int,
    @ColumnInfo(name = "materialID", defaultValue = "0") val materialID: Int?,
    @ColumnInfo(name = "quantity", defaultValue = "0") val quantity: BigDecimal?,
    @ColumnInfo(name = "detailID", defaultValue = "0") val detailID: Int?,
) : Serializable

data class MaterialQuanPair (
    val M: Int,
    val Q : BigDecimal
)

@Entity
data class CosplayPhoto(
    @PrimaryKey(autoGenerate = true) var photoID: Int,
    @ColumnInfo(name = "costumeID", defaultValue = "0") val costumeID: Int?,
    @ColumnInfo(name = "photo", defaultValue = "0") val photo: String?,
)