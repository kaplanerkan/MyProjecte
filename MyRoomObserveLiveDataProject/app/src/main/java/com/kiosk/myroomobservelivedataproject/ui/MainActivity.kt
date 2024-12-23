package com.kiosk.myroomobservelivedataproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiosk.myroomobservelivedataproject.R
import com.kiosk.myroomobservelivedataproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    private var currentUser: User? = null


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

        // Initialize RecyclerView
        userAdapter = UserAdapter { user -> onUserClicked(user) }
        binding.recyclerView.adapter = userAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Database and ViewModel
        val userDao = UserDatabase.getDatabase(application).userDao()
        val userRepository = UserRepository(userDao)
        userViewModel = UserViewModel(userRepository)

        // Fetch all users when the activity starts
        userViewModel.fetchAllUsers()
        userViewModel.allUsers.observe(this) { users ->
            userAdapter.submitList(users) // Use submitList here
        }

        // Add User Button Click
        binding.buttonAdd.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val user = User(0, name, email) // ID is auto-generated
                userViewModel.addUser(user)
                binding.editTextName.text?.clear()
                binding.editTextEmail.text?.clear()
                Toast.makeText(this@MainActivity, "User added successfull", Toast.LENGTH_SHORT).show()
                userViewModel.fetchAllUsers()

            } else {
                Toast.makeText(this@MainActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }


        }

        // Update User Button Click
        binding.buttonUpdate.setOnClickListener {
            currentUser?.let {
                val name = binding.editTextName.text.toString()
                val email = binding.editTextEmail.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val updatedUser = it.copy(name = name, email = email)
                    userViewModel.updateUser(updatedUser)
                    binding.editTextName.text?.clear()
                    binding.editTextEmail.text?.clear()
                    Toast.makeText(this@MainActivity, "User updated", Toast.LENGTH_SHORT).show()

                    userViewModel.fetchAllUsers()

                } else {
                    Toast.makeText(this@MainActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this@MainActivity, "No user selected for update", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete User Button Click
        binding.buttonDelete.setOnClickListener {
            currentUser?.let {
                userViewModel.deleteUser(it)
                binding.editTextName.text?.clear()
                binding.editTextEmail.text?.clear()

                Toast.makeText(this@MainActivity, "User deleted", Toast.LENGTH_SHORT).show()

                currentUser = null // Clear the current user

                userViewModel.fetchAllUsers()

            } ?: run {
                Toast.makeText(this@MainActivity, "No user selected for deletion", Toast.LENGTH_SHORT).show()
            }
        }

    }



    private fun onUserClicked(user: User) {
        currentUser = user
        binding.editTextName.setText(user.name)
        binding.editTextEmail.setText(user.email)
    }


}