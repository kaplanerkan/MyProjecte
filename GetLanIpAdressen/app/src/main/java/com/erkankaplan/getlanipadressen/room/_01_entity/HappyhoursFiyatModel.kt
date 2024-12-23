package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "happyhours_prices") class HappyhoursFiyatModel(
    @PrimaryKey(autoGenerate = true) @SerializedName("InternId") val internId : Int = 0,
    @SerializedName("HappyhourId") val happyhourId : Int = 0,
    @SerializedName("UrunId") val urunId : Int = 0,
    @SerializedName("FiyatSira") val fiyatSira : Int = 0,
    @SerializedName("Fiyat") val fiyat : Double = 0.0,

    )

