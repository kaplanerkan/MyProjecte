package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ingredient_prices")
data class IngredientPriceModel(
    @PrimaryKey
    @SerializedName("Id") val id: Int,

    @SerializedName("ProductId") val productId: Int,
    @SerializedName("PriceRow") val priceRow: Int,
    @SerializedName("Price") val price: Double,
    @SerializedName("Description") val description: String,
    @SerializedName("LastUpdate") val lastUpdate: String // ISO 8601 format
)
