package com.kiosk.mysimplelivedatamitviewmodel.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var number: Int = 0

    private val _squareValue = MutableLiveData<Int>()
    val squareValue: LiveData<Int> = _squareValue

    fun addNumber() {
        number++
        _squareValue.postValue(number * number)
    }
}