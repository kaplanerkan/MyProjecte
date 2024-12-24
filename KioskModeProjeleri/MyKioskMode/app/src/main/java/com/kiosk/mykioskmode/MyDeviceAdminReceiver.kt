package com.kiosk.mykioskmode

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

class MyDeviceAdminReceiver  : DeviceAdminReceiver() {


    companion object {
        fun getComponentName(context : Context) : ComponentName {
            return ComponentName(context.applicationContext, MyDeviceAdminReceiver::class.java)
        }

        private val TAG = MyDeviceAdminReceiver::class.java.simpleName
    }

    override fun onLockTaskModeEntering(context : Context, intent : Intent, pkg : String) {
        super.onLockTaskModeEntering(context, intent, pkg)
        Log.e(TAG, "onLockTaskModeEntering")
    }

    override fun onLockTaskModeExiting(context : Context, intent : Intent) {
        super.onLockTaskModeExiting(context, intent)
        Log.e(TAG, "onLockTaskModeExiting")
    }




    override fun onEnabled(context: Context, intent: Intent) {
        Log.e(TAG, "AdminContextManager onEnabled")
        super.onEnabled(context, intent)
       // context.saveSharedKey("isAdmin",true)
        //App.getPreferences().edit().putBoolean(App.ADMIN_ENABLED, true).commit(); //App.getPreferences() returns the sharedPreferences

    }

    override fun onDisabled(context: Context, intent: Intent) {
        Log.e(TAG, "AdminContextManager onDisabled")
        super.onDisabled(context, intent)
        // context.saveSharedKey("isAdmin",false)
    }





}