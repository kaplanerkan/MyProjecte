package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "happyhours") data class HappyhoursModel(
    @PrimaryKey(autoGenerate = true) @SerializedName("InternId") val internId : Int = 0,

    @SerializedName("Aciklama") val aciklama : String = "",
    @SerializedName("VonTime") val vonTime : String = "",
    @SerializedName("BisTime") val bisTime : String = "",
    @SerializedName("Gun0") val gun0 : Int = 0,               // Pazartesi
    @SerializedName("Gun1") val gun1 : Int = 0,
    @SerializedName("Gun2") val gun2 : Int = 0,
    @SerializedName("Gun3") val gun3 : Int = 0,
    @SerializedName("Gun4") val gun4 : Int = 0,
    @SerializedName("Gun5") val gun5 : Int = 0,
    @SerializedName("Gun6") val gun6 : Int = 0,       // Pazar
    @SerializedName("Changed") val changed : Int = 0

)
