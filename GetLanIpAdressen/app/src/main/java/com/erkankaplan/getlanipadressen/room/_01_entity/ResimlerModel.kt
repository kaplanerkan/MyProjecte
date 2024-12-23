package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "resimler_model")
class ResimlerModel (

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ResimId") val ResimId : Int = 0,
    @SerializedName("FileName") val FileName : String? = null,
    @SerializedName("Image") val Image : String? = null,

    @SerializedName("Name") val Name : String? = null

)