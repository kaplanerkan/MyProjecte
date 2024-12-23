package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product_prices")
data class ProductPriceModel(

    @PrimaryKey
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("Id") val id: Int,



    @SerializedName("PriceRow") val priceRow: Int,
    @SerializedName("Price") val price: Double,
    @SerializedName("Description") val description: String,
    @SerializedName("LastUpdate") val lastUpdate: String, // ISO 8601 format


)
