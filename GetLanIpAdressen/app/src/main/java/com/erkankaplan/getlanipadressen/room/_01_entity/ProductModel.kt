package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "products")
data class ProductModel(
    @SerializedName("Id")
    @PrimaryKey val id: Int,

    @SerializedName("Name") val name: String,
    @SerializedName("Row") val row: Int,
    @SerializedName("ImageId") val imageId: Int,
    @SerializedName("ProductGroupId") val productGroupId: Int,
    @SerializedName("Price") val price: Double,
    @SerializedName("Description") val description: String,
    @SerializedName("IsMenu") val isMenu: Int,
    @SerializedName("IsStockOut") val isStockOut: Boolean,
    @SerializedName("MaxIngredientCount") val maxIngredientCount: Int,
    @SerializedName("ProductTypeId") val productTypeId: Int,
    @SerializedName("FileName") val fileName: String,
    @SerializedName("Image") val image: String,
    @SerializedName("OriginalFileName") val originalFileName: String,
    @SerializedName("IsKioskActive") val isKioskActive: Boolean,
    @SerializedName("FreeIngredientCount") val freeIngredientCount: Int,
    @SerializedName("IsMenuSubProduct") val isMenuSubProduct: Boolean,
    @SerializedName("LastUpdate") val lastUpdate: String,
    @SerializedName("MandatoryOptions") val mandatoryOptions: String,
    @SerializedName("Icindekiler") val icindekiler: String,
    @SerializedName("KioskTavsiyeEdilenUrun") val kioskTavsiyeEdilenUrun: Boolean,
    @SerializedName("KioskIsLinkToUrun") val kioskIsLinkToUrun: Boolean,
    @SerializedName("KioskLinkToUrunler") val kioskLinkToUrunler: String,
    @SerializedName("ProduktRealId") val produktRealId: Int,
    @SerializedName("KioskDummyBool") val kioskDummyBool: Boolean,
    @SerializedName("KioskOnlyTakeAway") val kioskOnlyTakeAway: Boolean,
    @SerializedName("GroupName") val groupName: String,
    @SerializedName("ProductImage") val productImage: String

)
