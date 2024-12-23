package com.erkankaplan.getlanipadressen.room._02_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientLimitModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductPriceModel

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductIngredientLimits( productIngredientLimits: List<ProductIngredientLimitModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductGroups(productGroups: List<ProductGroupModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductPrices(productPrices: List<ProductPriceModel>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductIngredients(productIngredients: List<ProductIngredientModel>)


    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductModel>>
    @Query("SELECT * FROM product_groups gp WHERE gp.isKioskActive=1 ORDER BY gp.`row` ASC")
    fun getAllProductGroups(): LiveData<List<ProductGroupModel>>
    @Query("SELECT * FROM product_prices")
    fun getAllProductPrices(): LiveData<List<ProductPriceModel>>
    @Query("SELECT * FROM product_ingredient_limits")
    fun getAllProductIngredientLimits(): LiveData<List<ProductIngredientLimitModel>>
    @Query("SELECT * FROM product_ingredients")
    fun getAllProductIngredients(): LiveData<List<ProductIngredientModel>>


    @Query("SELECT * FROM products WHERE productGroupId = :groupId")
    fun getProductsByGroupId(groupId: Int): LiveData<List<ProductModel>>


    @Query ("DELETE FROM product_groups")
    suspend fun deleteAllProductGroups()
    @Query ("DELETE FROM product_ingredient_limits")
    suspend fun deleteAllProductIngredientLimits()
    @Query ("DELETE FROM product_ingredients")
    suspend fun deleteAllProductIngredients()
    @Query ("DELETE FROM products")
    suspend fun deleteAllProducts()
    @Query ("DELETE FROM product_prices")
    suspend fun deleteAllProductPrices()




}