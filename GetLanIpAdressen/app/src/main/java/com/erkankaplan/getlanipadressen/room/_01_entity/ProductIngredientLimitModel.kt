package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product_ingredient_limits") class ProductIngredientLimitModel(

    @PrimaryKey(autoGenerate = true) @SerializedName("ProductId") val productId : Int = 0,

    @SerializedName("IngredientGroupId") val ingredientGroupId : Int = 0,
    @SerializedName("MaxLimit") val maxLimit : Int = 0,
    @SerializedName("FreeIngredientLimit") val freeIngredientLimit : Int = 0,
    @SerializedName("MinLimit") val minLimit : Int = 0

)
