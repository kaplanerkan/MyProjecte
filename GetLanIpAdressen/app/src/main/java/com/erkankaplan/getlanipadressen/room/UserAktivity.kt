package com.erkankaplan.getlanipadressen.room


import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.erkankaplan.getlanipadressen.R
import com.erkankaplan.getlanipadressen.databinding.ActivityUserAktivityBinding
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel
import com.erkankaplan.getlanipadressen.room._05_viewmodels.ViewModelUserModel

import com.erkankaplan.getlanipadressen.room._06_adapters.UserAdapter
import es.dmoral.toasty.Toasty

@Suppress("SameParameterValue")
class UserAktivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserAktivityBinding
    val userViewModel : ViewModelUserModel by viewModels()
    private lateinit var adapter : UserAdapter

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //setContentView(R.layout.activity_user_aktivity)

        binding = ActivityUserAktivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initviews()

    }

    private fun initviews() {
        Toasty.Config.getInstance().tintIcon(true) // optional (apply textColor also to the icon)
            .setTextSize(18) // optional
            .allowQueue(true) // optional (prevents several Toastys from queuing)
            .setGravity(Gravity.CENTER) // optional (set toast gravity, offsets are optional)
            .apply() // required


        // Yontem 1
        // Adapter'ı oluştururken onDeleteClick fonksiyonunu tanımlıyoruz
//        adapter = UserAdapter{user ->
//            userViewModel.deleteUser(user)
//        }

        // Yontem 2
        adapter = UserAdapter { user ->
            runOnUiThread {
                Toasty.info(this@UserAktivity,
                            "Selected user: ${user.userName}, Age: ${user.password}",
                            Toast.LENGTH_SHORT).show()
            }

        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Yontem 1
//        userViewModel.allUsers.observe(this, Observer { users ->
//            users?.let { adapter.setUsers(it) }
//        })
        // Yontem 2
        userViewModel.allUsers.observe(this) { users ->
            //users?.let { adapter.setUsers(it) }
            users?.let { adapter.updateUsers(it) }
        }

        // Yeni kullanıcı eklemek için örnek
        binding.btnAddManuel.setOnClickListener {
            val randomAge = (18..65).random()
            val randomName = randomString(5)
            val user = UserModel(id = randomAge, userName = randomName, password = randomName, authorization = 0)
            userViewModel.insertUser(user)
        }
        binding.addButton.setOnClickListener {
                // Yontem 2: Fragment ile ekleme
                val dialog = AddUserDialogFragment()
                dialog.show(supportFragmentManager, "AddUserDialog")

        }
        // Insert in Observe si
        userViewModel.userInsertResult.observe(this) { result ->
            if (result != null && result > 0) {
                Toasty.success(this, "$result User added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toasty.error(this, "Failed to add user", Toast.LENGTH_SHORT).show()
            }
        }

        // Yontem 2
        // Delete butonuna tıklandığında seçilen kullanıcıyı silmek için
        binding.deleteButton.setOnClickListener {
            val selectedUser = adapter.getSelectedUser()
            if (selectedUser != null) {
                // Yontem 1, dogrudan siler
//                userViewModel.delete(selectedUser) // ViewModel aracılığıyla kullanıcıyı sil
//                adapter.removeSelectedUser() // Adapter'dan kullanıcıyı kaldır

                showDeleteConfirmDialog(selectedUser)

            }


        }


        binding.editButton.setOnClickListener {
            val selectedUser = adapter.getSelectedUser()


            // Yontem 1
            //showEditUserDialog(selectedUser!!)


            // Yontem 2
            //val dialog = selectedUser?.let { it1 -> EditUserDialogFragment(it1) }
            //dialog?.show(supportFragmentManager, "EditUserDialog")
            val dialog = EditUserDialogFragment(selectedUser!!,
                                                object : EditUserDialogFragment.OnUserUpdateListener {
                                                    override fun onUserUpdated(updatedUser : UserModel) {
                                                        userViewModel.updateUser(updatedUser)
                                                    }
                                                })
            dialog.show(supportFragmentManager, "EditUserDialog")

            /***
             *  veya bi sekilde  (EditUserDialogFragment.OnUserUpdateListener implement edilmesi gerekli)

            val dialog = EditUserDialogFragment(user, this)
            dialog.show(supportFragmentManager, "EditUserDialog")

            override fun onUserUpdated(updatedUser: UserEntityModel) {
            // Güncellenmiş kullanıcıyı işleyin
            // Örneğin, ViewModel veya doğrudan veri tabanı güncellemesi yapabilirsiniz
            }
             */


        }


    }

    // Yontem 1: Buda guzel bir yontem, Fragment olarakda guzel bir yontem
    private fun showEditUserDialog(user : UserModel) {
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_edit_user, null)
        val editName = dialogLayout.findViewById<EditText>(R.id.editName)
        val editAge = dialogLayout.findViewById<EditText>(R.id.editAge)
        val editId = dialogLayout.findViewById<EditText>(R.id.editId)
        val editAuthorization = dialogLayout.findViewById<EditText>(R.id.editAuthorization)

        editName.setText(user.userName)
        editAge.setText(user.password)
        editId.setText(user.id.toString())
        editAuthorization.setText(user.authorization.toString())

        AlertDialog.Builder(this).setTitle("Edit User").setView(dialogLayout).setPositiveButton("Update") { _, _ ->
            val updatedName = editName.text.toString()
            val updatePasswort = editAge.text.toString()
            val updatedUser = UserModel(id = user.id, userName = updatedName, password = updatePasswort, authorization = user.authorization)
            userViewModel.updateUser(updatedUser)
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }.create().show()
    }

    // Kullanıcıdan silme onayi almak için bir popup gösteren fonksiyon
    private fun showDeleteConfirmDialog(user : UserModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete this data?  \n\n ${user.id} \n ${user.userName} \n ${user.password}")

        builder.setPositiveButton("DELETE") { dialog, _ ->
            // Kullanıcı 'Evet' dediğinde yapılacak işlemler
            dialog.dismiss()
            runOnUiThread {
                userViewModel.deleteUser(user) // ViewModel aracılığıyla kullanıcıyı sil
                adapter.removeSelectedUser() // Adapter'dan kullanıcıyı kaldır
            }

        }

        builder.setNegativeButton("CANCEL") { dialog, _ ->
            // Kullanıcı 'Hayır' dediğinde yapılacak işlemler
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun randomString(length : Int) : String {
        val chars = ('a'..'z') + ('A'..'Z')
        return (1..length).map { chars.random() }.joinToString("")
    }
}