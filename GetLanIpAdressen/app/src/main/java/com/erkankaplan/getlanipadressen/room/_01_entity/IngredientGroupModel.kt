package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ingredient_groups")
data class IngredientGroupModel(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("ImageId")
    val imageId: Int,

    @SerializedName("Row")
    val row: Int,

    @SerializedName("FileName")
    val fileName: String,

    @SerializedName("Image")
    val image: String,

    @SerializedName("OriginalFileName")
    val originalFileName: String,

    @SerializedName("GroupName")
    val groupName: String,

    @SerializedName("FreeIngredientLimit")
    val freeIngredientLimit: Int,

    @SerializedName("ProductImage")
    val productImage: String


)
