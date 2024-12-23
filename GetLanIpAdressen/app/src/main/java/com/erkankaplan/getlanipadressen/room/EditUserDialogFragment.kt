package com.erkankaplan.getlanipadressen.room

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.erkankaplan.getlanipadressen.databinding.DialogEditUserBinding
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel

class EditUserDialogFragment(private val user : UserModel, private val listener : OnUserUpdateListener) : DialogFragment() {


    interface OnUserUpdateListener {
        fun onUserUpdated(updatedUser : UserModel)
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState : Bundle?) : Dialog {
        // ViewBinding ile dialog layout'unu oluşturun
        val binding = DialogEditUserBinding.inflate(LayoutInflater.from(context))

        // User bilgilerini ViewBinding'e yükleyin
        binding.editName.setText(user.userName)
        binding.editAge.setText(user.password.toString())
        binding.editId.setText(user.id.toString())

        // AlertDialog.Builder ile dialog oluşturun
        return AlertDialog.Builder(requireContext()).setTitle("Edit User Fragment").setView(binding.root).setPositiveButton(
                "Update") { _, _ ->
                // Güncellenmiş veriyi alın ve listener'a gönderin
                val updatedUser = UserModel(id = user.id,
                                            userName = binding.editName.text.toString(),
                                            password = binding.editAge.text.toString(),
                                            authorization = user.authorization)

                listener.onUserUpdated(updatedUser)
            }.setNegativeButton("Cancel", null).create()
    }
}