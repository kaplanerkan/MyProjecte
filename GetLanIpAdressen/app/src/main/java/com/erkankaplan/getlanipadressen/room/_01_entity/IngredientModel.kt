package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ingredients")
data class IngredientModel(

    @PrimaryKey @SerializedName("Id") val id : Int,

    @SerializedName("Name") val name : String,

    @SerializedName("Row") val row : Int,

    @SerializedName("ImageId") val imageId : Int,

    @SerializedName("IngredientGroupId") val ingredientGroupId : Int,

    @SerializedName("Price") val price : Double,

    @SerializedName("FileName") val fileName : String,

    @SerializedName("Image") val image : String,

    @SerializedName("OriginalFileName") val originalFileName : String,

    @SerializedName("StokdaMevcut") val stokdaMevcut : Boolean,

    @SerializedName("CesniGrupIsmi") val cesniGrupIsmi : String,

    @SerializedName("OneTimeChoice") val oneTimeChoice : Boolean,

    @SerializedName("IsKioskAktive") val isKioskAktive : Boolean,

    @SerializedName("GroupName") val groupName : String,

    @SerializedName("ProductImage") val productImage : String,

)