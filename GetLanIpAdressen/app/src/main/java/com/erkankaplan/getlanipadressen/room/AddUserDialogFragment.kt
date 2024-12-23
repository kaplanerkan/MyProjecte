package com.erkankaplan.getlanipadressen.room

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.erkankaplan.getlanipadressen.databinding.DialogAddUserBinding
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel
import com.erkankaplan.getlanipadressen.room._05_viewmodels.ViewModelUserModel
import es.dmoral.toasty.Toasty

class AddUserDialogFragment : DialogFragment() {

    private var _binding : DialogAddUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel : ViewModelUserModel

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(),  // Genişliği %85 yapıyoruz
            // ViewGroup.LayoutParams.WRAP_CONTENT                   // Yükseklik içerikle sarılacak
                                  (resources.displayMetrics.heightPixels * 0.50).toInt()  // Yüksekliği %50 yapıyoruz

        )
        dialog?.window?.setGravity(Gravity.CENTER)                  // Ortalıyoruz

        isCancelable = false                                        // ile dialogun kullanıcı tarafından iptal edilmesini engelliyoruz.
        dialog?.setCanceledOnTouchOutside(false)

    }

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        _binding = DialogAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toasty.Config.getInstance().tintIcon(true) // optional (apply textColor also to the icon)
            .setTextSize(18) // optional
            .allowQueue(true) // optional (prevents several Toastys from queuing)
            .setGravity(Gravity.CENTER) // optional (set toast gravity, offsets are optional)
            .apply() // required


        userViewModel = ViewModelProvider(requireActivity())[ViewModelUserModel::class.java]

        binding.ekleButton.setOnClickListener {
            val edUsername = binding.edUsername.text.toString()
            val edPassword = binding.edPassword.text.toString()
            val edAuthorization = binding.edAuthorization.text.toString()

            if (edUsername.isNotEmpty() && edPassword.isNotEmpty() && edAuthorization.isNotEmpty()) {
                val user = UserModel(userName = edUsername,
                                     password = edPassword,
                                     authorization = edAuthorization.toInt())
                userViewModel.insertUser(user)
                dismiss()
            } else {

                /*
                 * Bos bir alan varsa hata mesajı veriyoruz ve guzel bir efekt saglıyoruz
                 */
                if (edUsername.isEmpty()) {
                    binding.edUsername.error = "Kullanıcı adı boş olamaz"
                }
                if (edPassword.isEmpty()) {
                    binding.edPassword.error = "Şifre boş olamaz"
                }
                if (edAuthorization.isEmpty()) {
                    binding.edAuthorization.error = "Yetki boş olamaz"
                }

                Toasty.error(requireContext(),
                             "Lütfen tüm alanları doldurun",
                             Toasty.LENGTH_SHORT,
                             true).show()
            }
        }


        binding.cancelButton.setOnClickListener {
            dismiss()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}