package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product_groups")
data class ProductGroupModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("Id") val id: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("ImageId") val imageId: Int,
    @SerializedName("FileName") val fileName: String,
    @SerializedName("Image") val image: String,
    @SerializedName("Row") val row: Int,
    @SerializedName("OriginalFileName") val originalFileName: String,
    @SerializedName("LastUpdate") val lastUpdate: String,
    @SerializedName("IsKioskActive") val isKioskActive: Boolean,
    @SerializedName("GroupName") val groupName: String,
    @SerializedName("ProductImage") val productImage: String

)
