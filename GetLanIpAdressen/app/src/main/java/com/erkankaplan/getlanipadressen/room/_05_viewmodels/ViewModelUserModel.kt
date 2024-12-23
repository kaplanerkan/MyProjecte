package com.erkankaplan.getlanipadressen.room._05_viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.erkankaplan.getlanipadressen.retrofit._01_api_service.ApiResponse
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel
import com.erkankaplan.getlanipadressen.room._03_roomdatabase.KioskAppDatabase
import kotlinx.coroutines.launch

class ViewModelUserModel(application: Application) : AndroidViewModel(application) {

    private val userModelDao = KioskAppDatabase.getDatabase(application).userModelDao()
    val allUsers : LiveData<List<UserModel>> = userModelDao.getAllUsers()














    private val _userInsertResult = MutableLiveData<Long?>()
    val userInsertResult: LiveData<Long?> get() = _userInsertResult

    fun insertUser(userModel: UserModel) {
        viewModelScope.launch {
            try {
                val rowsAffected = userModelDao.insert(userModel)
                _userInsertResult.postValue(rowsAffected)
            } catch (e: Exception) {
                _userInsertResult.postValue(-1)
                e.printStackTrace()
            }
        }
    }

    private val _userDeleteResult = MutableLiveData<Int?>()
    val userDeleteResult: LiveData<Int?> get() = _userDeleteResult
    fun deleteUser(userModel: UserModel) {
        viewModelScope.launch {
            try {
                val rowsAffected = userModelDao.delete(userModel)
                _userDeleteResult.postValue(rowsAffected)
            } catch (e: Exception) {
                _userDeleteResult.postValue(-1)
                e.printStackTrace()
            }
        }
    }


    private val _userUpdateResult = MutableLiveData<Int?>()
    val userUpdateResult: LiveData<Int?> get() = _userUpdateResult

    fun updateUser(userModel: UserModel) {
        viewModelScope.launch {
            try {
                val rowsAffected = userModelDao.update(userModel)
                _userUpdateResult.postValue(rowsAffected)
            } catch (e: Exception) {
                _userUpdateResult.postValue(-1)
                e.printStackTrace()
            }
        }
    }


    // DELETE degiskenleri, silindigi zaman haber vermek icin
    private val _deleteResult = MutableLiveData<Int?>()
    val deleteResult: LiveData<Int?> get() = _deleteResult

    fun deleteAllUsers() {
        viewModelScope.launch {
            try {
                val rowsAffected =  userModelDao.deleteAllUsers()
                //_deleteResult.value=rowsAffected
                _deleteResult.postValue(rowsAffected)
            }catch (e: Exception) {
                e.printStackTrace()
                _deleteResult.postValue(-1) // Hata durumunda -1 döndür
            }
        }
    }

    fun fetchUserModelFromApi(apiResponse : ApiResponse) {
        viewModelScope.launch {
            try {
                //val apiResponse = RetrofitInstance.api.getApiResponse()
                //val apiResponse = RetrofitInstance.getInstance().getApiResponse()
                userModelDao.insertAllUsers(apiResponse.users)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }







}