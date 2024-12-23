package com.erkankaplan.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erkankaplan.adapter.IpAdreslerModel


class IpsViewModel : ViewModel() {

    // private val ipAdreslerList = mutableListOf<IpAdreslerModel>()


    private val _ipAdreslerList = MutableLiveData<MutableList<IpAdreslerModel>>(mutableListOf())
    val ipAdreslerList : MutableLiveData<MutableList<IpAdreslerModel>> get() = _ipAdreslerList

    fun addIp(urun : IpAdreslerModel) {
        val currentList = _ipAdreslerList.value ?: mutableListOf()
        currentList.add(urun)
        _ipAdreslerList.value = currentList // LiveData'yı güncelle
    }

    fun removeIp(urun : IpAdreslerModel) {
        val currentList = _ipAdreslerList.value ?: mutableListOf()
        currentList.remove(urun)
        _ipAdreslerList.value = currentList // LiveData'yı güncelle
    }

    fun clearIpList() {
        _ipAdreslerList.value = mutableListOf()
    }


//    fun addIp(ip: IpAdreslerModel) {
//        ipAdreslerList.add(ip)
//    }
//
//    fun getIpList(): List<IpAdreslerModel> {
//        return ipAdreslerList
//    }
//    fun removeIp(ip: IpAdreslerModel) {
//        ipAdreslerList.remove(ip)
//    }
//    fun clearIpList() {
//        ipAdreslerList.clear()
//    }

}