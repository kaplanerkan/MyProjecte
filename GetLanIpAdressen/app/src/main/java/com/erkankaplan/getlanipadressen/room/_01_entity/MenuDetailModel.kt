package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "menu_details")
data class MenuDetailModel(
    @PrimaryKey
    @SerializedName("Id") val id: Int,

    @SerializedName("MenuId") val menuId: Int,
    @SerializedName("ProductId") val productId: Int,
    @SerializedName("Price") val price: Double,
    @SerializedName("Selection") val selection: Int,
    @SerializedName("ProductPriceRow") val productPriceRow: Int,
    @SerializedName("LastUpdate") val lastUpdate: String // ISO 8601 format
)