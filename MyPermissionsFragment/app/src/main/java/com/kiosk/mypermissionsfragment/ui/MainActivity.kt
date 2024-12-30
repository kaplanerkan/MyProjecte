package com.kiosk.mypermissionsfragment.ui

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_PHONE_STATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kiosk.mypermissionsfragment.PermissionCallback
import com.kiosk.mypermissionsfragment.R
import com.kiosk.mypermissionsfragment.askPermissions
import com.kiosk.mypermissionsfragment.databinding.ActivityMainBinding
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

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


    private fun initViews(){

        setToastSettings()


        supportFragmentManager
            .askPermissions(
                arrayOf(
                    READ_EXTERNAL_STORAGE,
                    READ_PHONE_STATE,
                    ACCESS_NETWORK_STATE,
                    ACCESS_WIFI_STATE,
                ),
                // 2 tane interface callback var,
                object : PermissionCallback {
                    override fun onPermissionGranted() {
                        //Do required operation
                        Log.e("MainActivity", "onPermissionGranted: ")
                        Toasty.info(this@MainActivity, "ALL PERMISSIONS GRANTED ", Toast.LENGTH_SHORT, true).show()
                    }

                    override fun onPermissionDenied(deniedPermissionList : ArrayList<String>) {
                        //Do required operation
                        for (permission in deniedPermissionList) {
                            Log.e("MainActivity", "onPermissionDenied: $permission")
                        }

                        Log.e("MainActivity", "onPermissionDenied: $deniedPermissionList")

                        Toasty.info(this@MainActivity,
                                    "onPermissionDenied: $deniedPermissionList",
                                    Toast.LENGTH_SHORT, true).show()
                    }

                    override fun onPermissionDeniedForever() {
                        //Do required operation
                        Log.e("MainActivity", "onPermissionDeniedForever: ")
                        Toasty.info(this@MainActivity, "onPermissionDeniedForever: ", Toast.LENGTH_SHORT, true).show()

                        //startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS))
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.setData(uri)
                        startActivity(intent)
                    }
                }
            )


    }


    private fun setToastSettings(){
        Toasty.Config.getInstance()
            .tintIcon(true) // optional (apply textColor also to the icon)
            //.setToastTypeface(@NonNull Typeface typeface) // optional
            .setTextSize(24) // optional
            .allowQueue(true) // optional (prevents several Toastys from queuing)
            .setGravity(Gravity.CENTER) // optional (set toast gravity, offsets are optional)
            .supportDarkTheme(true) // optional (whether to support dark theme or not)
            .setRTL(true) // optional (icon is on the right)
            .apply(); // required
    }

}