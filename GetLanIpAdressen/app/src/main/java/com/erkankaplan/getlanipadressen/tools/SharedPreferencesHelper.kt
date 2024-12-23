package com.erkankaplan.getlanipadressen.tools

import android.content.Context


/*


SharedPreferencesHelper Kullanımı

// SharedPreferencesHelper nesnesini oluşturma
val sharedPreferencesHelper = SharedPreferencesHelper(context)

// Veri kaydetme
sharedPreferencesHelper.putString("username", "ChatGPT")
sharedPreferencesHelper.putInt("userAge", 30)
sharedPreferencesHelper.putBoolean("isLoggedIn", true)

// Veri okuma
val username = sharedPreferencesHelper.getString("username", "DefaultUser")
val userAge = sharedPreferencesHelper.getInt("userAge", 0)
val isLoggedIn = sharedPreferencesHelper.getBoolean("isLoggedIn", false)

// Sonuçları loglama
Log.d("SharedPreferencesHelper", "Username: $username, Age: $userAge, Logged In: $isLoggedIn")

// Veri silme
sharedPreferencesHelper.remove("username")

// Tüm verileri temizleme
sharedPreferencesHelper.clear()




 */




class SharedPreferencesHelper(context : Context) {


    // SharedPreferences nesnesini oluşturuyoruz
    private val sharedPreferences = context.getSharedPreferences("KioskPreferences",
                                                                 Context.MODE_PRIVATE)

    // String kaydetme metodu
    fun putString(key : String, value : String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    // Int kaydetme metodu
    fun putInt(key : String, value : Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            apply()
        }
    }

    // Boolean kaydetme metodu
    fun putBoolean(key : String, value : Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    // String okuma metodu
    fun getString(key : String, defaultValue : String) : String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    // Int okuma metodu
    fun getInt(key : String, defaultValue : Int) : Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Boolean okuma metodu
    fun getBoolean(key : String, defaultValue : Boolean) : Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Veri silme metodu
    fun remove(key : String) {
        with(sharedPreferences.edit()) {
            remove(key)
            apply()
        }
    }

    // Tüm verileri temizleme metodu
    fun clear() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }


}