package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product_ingredients")
data class ProductIngredientModel(
    @PrimaryKey
    @SerializedName("Id") val id: Int,

    @SerializedName("ProductId") val productId: Int,
    @SerializedName("IngredientId") val ingredientId: Int,
    @SerializedName("UseOriginalPrice") val useOriginalPrice: Int,
    @SerializedName("Price1") val price1: Double,
    @SerializedName("Price2") val price2: Double,
    @SerializedName("Price3") val price3: Double,
    @SerializedName("Price4") val price4: Double,
    @SerializedName("Price5") val price5: Double,
    @SerializedName("LastUpdate") val lastUpdate: String // ISO 8601 format
)
