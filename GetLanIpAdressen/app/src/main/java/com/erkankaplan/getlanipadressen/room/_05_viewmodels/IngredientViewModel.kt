package com.erkankaplan.getlanipadressen.room._05_viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.erkankaplan.getlanipadressen.retrofit._01_api_service.ApiResponse
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientPriceModel
import com.erkankaplan.getlanipadressen.room._03_roomdatabase.KioskAppDatabase
import kotlinx.coroutines.launch

class IngredientViewModel(application : Application) : AndroidViewModel(application){

    private val ingredientDao = KioskAppDatabase.getDatabase(application).ingredientDao()

    // Bunlar LiveData olacak ve Observe de dinlenecek
    val allIngredients: LiveData<List<IngredientModel>> = ingredientDao.getAllIngredients()
    val allIngredientGroups: LiveData<List<IngredientGroupModel>> = ingredientDao.getAllIngredientsGroup()
    val allIngredientPrices: LiveData<List<IngredientPriceModel>> = ingredientDao.getAllIngredientPrices()

    fun fetchIngredientsPriceFromApi(apiResponse : ApiResponse) {
        // Api den verileri çek ve insert et
        viewModelScope.launch {
            try {
                ingredientDao.insertAllIngredientPrices(apiResponse.ingredientPrices)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchIngredientsGroupFromApi(apiResponse : ApiResponse) {
        // Api den verileri çek ve insert et
        viewModelScope.launch {
            try {
                ingredientDao.insertAllProductGroups(apiResponse.ingredientGroups)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchIngredientsFromApi(apiResponse : ApiResponse) {
        // Api den verileri çek ve insert et
        viewModelScope.launch {
            try {
                ingredientDao.insertAllIngredients(apiResponse.ingredients)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Tüm cesnileri, cesit gruplarını ve fiyatlarını sil
     */
    fun deleteAllIngredients() {
        viewModelScope.launch {
            try {
                ingredientDao.deleteAllIngredients()
                ingredientDao.deleteAllIngredientGroups()
                ingredientDao.deleteAllIngredientPrices()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }







}