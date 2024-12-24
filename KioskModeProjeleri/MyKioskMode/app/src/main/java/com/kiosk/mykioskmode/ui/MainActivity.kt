package com.kiosk.mykioskmode.ui

import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.os.UserManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.kiosk.mykioskmode.MyDeviceAdminReceiver
import com.kiosk.mykioskmode.R
import com.kiosk.mykioskmode.databinding.ActivityMainBinding



/*
https://github.com/mrugacz95/kiosk


SET ETME
 .\adb.exe shell dpm set-device-owner com.kiosk.mykioskmode/.MyDeviceAdminReceiver

SILME
 .\adb.exe shell dpm remove-active-admin com.kiosk.mykioskmode/.MyDeviceAdminReceiver

If everything has gone well we should be able to see our application in the list of device's administrators in
Settings → Security → Device admin apps.



 */




class MainActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName : ComponentName
    private lateinit var mDevicePolicyManager : DevicePolicyManager

    private lateinit var binding : ActivityMainBinding


    companion object {
        const val LOCK_ACTIVITY_KEY = "LOCK_ACTIVITY_KEY"
    }


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }


    private fun initViews() {

        setDeviceManager()

        setOverlayDisplay()

        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        mDevicePolicyManager.removeActiveAdmin(mAdminComponentName)

        val isAdmin = isAdmin()
        if (isAdmin) {
            Snackbar.make(binding.root, R.string.device_owner, Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(binding.root, R.string.not_device_owner, Snackbar.LENGTH_SHORT).show()
        }
        binding.btStartLockTask.setOnClickListener {
            setKioskPolicies(true, isAdmin)
        }
        binding.btStopLockTask.setOnClickListener {
            setKioskPolicies(false, isAdmin)
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            intent.putExtra(LOCK_ACTIVITY_KEY, false)
            startActivity(intent)
        }

    }


    private fun setOverlayDisplay() {

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:$packageName"))
            startActivity(intent)

        }

    }


    private fun isAdmin() = mDevicePolicyManager.isDeviceOwnerApp(packageName)

    private fun setKioskPolicies(enable : Boolean, isAdmin : Boolean) {
        if (isAdmin) {
            setRestrictions(enable)
            enableStayOnWhilePluggedIn(enable)
            setUpdatePolicy(enable)
            setAsHomeApp(enable)
            setKeyGuardEnabled(enable)
        }
        setLockTask(enable, isAdmin)
        setImmersiveMode(enable)
    }

    // region restrictions
    private fun setRestrictions(disallow : Boolean) {
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow)
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow)
        setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow)
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow)
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow)
        mDevicePolicyManager.setStatusBarDisabled(mAdminComponentName, disallow)
    }

    private fun setUserRestriction(restriction : String, disallow : Boolean) = if (disallow) {
        mDevicePolicyManager.addUserRestriction(mAdminComponentName, restriction)
    } else {
        mDevicePolicyManager.clearUserRestriction(mAdminComponentName, restriction)
    }
    // endregion

    private fun enableStayOnWhilePluggedIn(active : Boolean) = if (active) {
        mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
                                              Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                                              (BatteryManager.BATTERY_PLUGGED_AC or BatteryManager.BATTERY_PLUGGED_USB or BatteryManager.BATTERY_PLUGGED_WIRELESS).toString())
    } else {
        mDevicePolicyManager.setGlobalSetting(mAdminComponentName,
                                              Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                                              "0")
    }

    private fun setLockTask(start : Boolean, isAdmin : Boolean) {
        if (isAdmin) {
            mDevicePolicyManager.setLockTaskPackages(mAdminComponentName,
                                                     if (start) arrayOf(packageName) else arrayOf())
        }
        if (start) {
            startLockTask()
        } else {
            stopLockTask()
        }
    }

    private fun setUpdatePolicy(enable : Boolean) {
        if (enable) {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
                                                       SystemUpdatePolicy.createWindowedInstallPolicy(
                                                           60,
                                                           120))
        } else {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName, null)
        }
    }

    private fun setAsHomeApp(enable : Boolean) {
        if (enable) {
            val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addCategory(Intent.CATEGORY_DEFAULT)
            }
            mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
                                                                intentFilter,
                                                                ComponentName(packageName,
                                                                              MainActivity::class.java.name))
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(mAdminComponentName,
                                                                           packageName)
        }
    }

    private fun setKeyGuardEnabled(enable : Boolean) {
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, !enable)
    }

    @Suppress("DEPRECATION") private fun setImmersiveMode(enable : Boolean) {
        if (enable) {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.decorView.systemUiVisibility = flags
        } else {
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.decorView.systemUiVisibility = flags
        }
    }




    private fun setDeviceManager() {
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val test = devicePolicyManager.isAdminActive(ComponentName(this,
                                                                   MyDeviceAdminReceiver::class.java)) && devicePolicyManager.isDeviceOwnerApp(
            packageName)





        startActivity(Intent().setComponent(ComponentName("com.android.settings",
                                                          "com.android.settings.DeviceAdminSettings")))

        Log.e("DEVICE", "DEVICE MANAGER:$test")

    }


}