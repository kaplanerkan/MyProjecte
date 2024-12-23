package com.erkankaplan.getlanipadressen.room._05_viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.erkankaplan.getlanipadressen.retrofit._01_api_service.ApiResponse
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientLimitModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductPriceModel
import com.erkankaplan.getlanipadressen.room._03_roomdatabase.KioskAppDatabase
import kotlinx.coroutines.launch

class ProductViewModel(application : Application) : AndroidViewModel(application) {

    private val productDao = KioskAppDatabase.getDatabase(application).productDao()


    // Bunlar LiveData olacak ve Observe de dinlenecek
    val allProductGroups : LiveData<List<ProductGroupModel>> = productDao.getAllProductGroups()
    val allProductIngredientLimits : LiveData<List<ProductIngredientLimitModel>> = productDao.getAllProductIngredientLimits()
    val allProductIngredients : LiveData<List<ProductIngredientModel>> = productDao.getAllProductIngredients()
    val allProducts : LiveData<List<ProductModel>> = productDao.getAllProducts()
    val allProductPrices : LiveData<List<ProductPriceModel>> = productDao.getAllProductPrices()


    fun deleteProduct() {
        viewModelScope.launch {
            try {
                productDao.deleteAllProducts()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }


    fun deleteProductGroups() {
        viewModelScope.launch {
            try {
                productDao.deleteAllProductGroups()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }



    fun getProductByGroupId(groupId : Int) : LiveData<List<ProductModel>> {
        return productDao.getProductsByGroupId(groupId)
    }






    // 01
    fun fetchProductGroupsFromApi(apiResponse : ApiResponse) {
        viewModelScope.launch {
            try {
                productDao.insertAllProductGroups(apiResponse.productGroups)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    // 02
    fun fetchProductIngredientLimitsFromApi(apiResponse : ApiResponse) {
        viewModelScope.launch {
            try {
                productDao.insertAllProductIngredientLimits(apiResponse.productIngredientLimits)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    // 03
    fun fetchProductIngredientsFromApi(apiResponse : ApiResponse) {
        viewModelScope.launch {
            try {
                productDao.insertAllProductIngredients(apiResponse.productIngredients)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }


    //04
    fun fetchProductsFromApi(apiResponse : ApiResponse) {
        viewModelScope.launch {
            try {
                //val apiResponse = RetrofitInstance.api.getApiResponse()
                //val apiResponse = RetrofitInstance.getInstance().getApiResponse()
                productDao.insertAllProducts(apiResponse.products)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }


    //05
    fun fetchProductPriceFromApi(apiResponse : ApiResponse) {
        viewModelScope.launch {
            try {
                //val apiResponse = RetrofitInstance.api.getApiResponse()
                //val apiResponse = RetrofitInstance.getInstance().getApiResponse()
                productDao.insertAllProductPrices(apiResponse.productPrices)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }


}