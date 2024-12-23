package com.erkankaplan.getlanipadressen.retrofit._01_api_service

import com.erkankaplan.getlanipadressen.room._01_entity.IngredientGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientPriceModel
import com.erkankaplan.getlanipadressen.room._01_entity.MenuDetailModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientLimitModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductPriceModel
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel
import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("productGroups") val productGroups: List<ProductGroupModel>,
    @SerializedName("products") val products: List<ProductModel>,
    @SerializedName("users") val users: List<UserModel>, // Diğer bölümler için de sınıflar oluşturabilirsiniz

    @SerializedName("ingredientGroups") val ingredientGroups: List<IngredientGroupModel>,

    @SerializedName("ingredients") val ingredients: List<IngredientModel>,

    @SerializedName("ingredientPrices") val ingredientPrices: List<IngredientPriceModel>,

    @SerializedName("menuDetails") val menuDetails: List<MenuDetailModel>,

    @SerializedName("productIngredients") val productIngredients: List<ProductIngredientModel>,
    @SerializedName("productPrices") val productPrices: List<ProductPriceModel>,

    @SerializedName("productIngredientLimits") val productIngredientLimits: List<ProductIngredientLimitModel>,
    @SerializedName("translations") val translations: List<Any>,
    @SerializedName("happyhours") val happyhours: List<Any>,
    @SerializedName("happyhoursFiyat") val happyhoursFiyat: List<Any>,
    @SerializedName("PagerTitle") val pagerTitle: String
)
