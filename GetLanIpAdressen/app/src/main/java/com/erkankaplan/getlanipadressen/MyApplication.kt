package com.erkankaplan.getlanipadressen

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {

        private var instance: MyApplication? = null

        fun myApplicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Uygulama genelinde yapılacak diğer işlemler
    }




}