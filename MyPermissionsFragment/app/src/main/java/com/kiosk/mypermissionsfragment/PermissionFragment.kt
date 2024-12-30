package com.kiosk.mypermissionsfragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kiosk.mypermissionsfragment.PermissionFragment.Companion.instanceOf

class PermissionFragment : Fragment() {

    companion object {

        private const val PERMISSION_LIST = "permission_list"
        private var mPermissionCallback : Any? = null

        @JvmStatic fun instanceOf(
            permissionList : Array<String>, permissionCallback : Any) : PermissionFragment {

            mPermissionCallback = permissionCallback
            return PermissionFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(PERMISSION_LIST, permissionList)
                }
            }
        }
    }

    private lateinit var mRequestedPermissions : Array<String>

    // Kullanımdan kaldırılmış onRequestPermissionsResult yerine ActivityResultLauncher kullanıyoruz
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val deniedPermissionList : ArrayList<String> = ArrayList()
        val deniedForeverList : ArrayList<String> = ArrayList()

        permissions.entries.forEach { entry ->
            val isGranted = entry.value
            if (!isGranted) {
                deniedPermissionList.add(entry.key)
                // İzin kalıcı olarak reddedilmiş mi kontrol et
                if (!shouldShowRequestPermissionRationale(entry.key)) {
                    deniedForeverList.add(entry.key)
                }
            }
        }

        mPermissionCallback?.also { callback ->
            when {
                deniedForeverList.isNotEmpty()    -> {
                    when (callback) {
                        is PermissionWithHandledDeniedForever -> {
                            AlertDialog.Builder(requireContext()).setMessage("Please enable all permissions from settings to proceed further.").setPositiveButton(
                                "Ok") { _, _ ->
                                requireActivity().openSettings()
                            }.setNegativeButton("Cancel", null).show()

                        }

                        is PermissionCallback                 -> {
                            callback.onPermissionDeniedForever()
                        }
                    }
                }

                deniedPermissionList.isNotEmpty() -> {
                    when (callback) {
                        is PermissionWithHandledDeniedForever -> callback.onPermissionDenied(
                            deniedPermissionList)

                        is PermissionCallback                 -> callback.onPermissionDenied(
                            deniedPermissionList)
                    }
                }

                else                              -> {
                    when (callback) {
                        is PermissionWithHandledDeniedForever -> callback.onPermissionGranted()
                        is PermissionCallback                 -> callback.onPermissionGranted()
                    }
                }
            }
        }
        // Fragment'ı kaldırma işlemini callback'lerden ayırdık
        removeCurrentFragment()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(PermissionLifecycle())
    }

    private fun removeCurrentFragment() {
        // parentFragmentManager kullanıyoruz
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun Activity.openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    private inner class PermissionLifecycle : LifecycleEventObserver {

        override fun onStateChanged(source : LifecycleOwner, event : Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE  -> {
                    if (mPermissionCallback != null) {
                        getRequestedPermissions()
                        askForPermissions()
                    }
                }

                Lifecycle.Event.ON_DESTROY -> {
                    mPermissionCallback = null
                    // Lifecycle observer'ı kaldır
                    source.lifecycle.removeObserver(this)
                }

                else                       -> {}
            }
        }

        /**
         * Get the list of requested permission as an array string from arguments.
         */
        private fun getRequestedPermissions() {
            arguments?.apply {
                mRequestedPermissions = getStringArray(PERMISSION_LIST)!!
            }
        }

        /**
         * Find the list of granted permission, if all permission are granted than pass callback method
         * onPermissionGranted.
         *
         * Request for all the permissions that are not granted.
         */
        private fun askForPermissions() {

            val permissionGrantedList : ArrayList<String> = ArrayList()

            //Find the list of granted permission and add to the array list.
            mRequestedPermissions.filter { permission ->
                (ContextCompat.checkSelfPermission(requireContext(),
                                                   permission) == PackageManager.PERMISSION_GRANTED)
            }.mapTo(permissionGrantedList) { it }

            //If all permission are already granted call onPermissionGranted
            if (permissionGrantedList.size == mRequestedPermissions.size) {
                mPermissionCallback?.let {
                    when (it) {
                        is PermissionWithHandledDeniedForever -> it.onPermissionGranted()
                        is PermissionCallback                 -> it.onPermissionGranted()
                    }
                }
            } else {
                //Requesting for non-granted permissions
                requestPermissionLauncher.launch(mRequestedPermissions)
            }
        }
    }

}


fun FragmentManager.askPermissions(permissionRequired : Array<String>, permissionCallback : Any) {
    val per = instanceOf(permissionRequired, permissionCallback)
    with(beginTransaction()) {
        add(per, "permission_alert")
        commit()
    }
}


interface PermissionWithHandledDeniedForever {
    fun onPermissionGranted()
    fun onPermissionDenied(deniedPermissionList : ArrayList<String>)
}

interface PermissionCallback {
    fun onPermissionGranted()
    fun onPermissionDenied(deniedPermissionList : ArrayList<String>)
    fun onPermissionDeniedForever()
}


