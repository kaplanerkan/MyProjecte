package com.erkankaplan.getlanipadressen.room._02_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erkankaplan.getlanipadressen.retrofit._01_api_service.ApiResponse
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientPriceModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel


@Dao
interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllIngredients(ingredients: List<IngredientModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductGroups(ingredientsGroup : List<IngredientGroupModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllIngredientPrices(ingredients: List<IngredientPriceModel>)


    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): LiveData<List<IngredientModel>>
    @Query("SELECT * FROM ingredient_groups")
    fun getAllIngredientsGroup(): LiveData<List<IngredientGroupModel>>
    @Query("SELECT * FROM ingredient_prices")
    fun getAllIngredientPrices(): LiveData<List<IngredientPriceModel>>





    @Query ("DELETE FROM ingredients")
    suspend fun deleteAllIngredients()
    @Query ("DELETE FROM ingredient_groups")
    suspend fun deleteAllIngredientGroups()
    @Query ("DELETE FROM ingredient_prices")
    suspend fun deleteAllIngredientPrices()
}