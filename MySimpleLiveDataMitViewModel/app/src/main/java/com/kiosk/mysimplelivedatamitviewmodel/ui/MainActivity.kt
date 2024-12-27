package com.kiosk.mysimplelivedatamitviewmodel.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kiosk.mysimplelivedatamitviewmodel.R
import com.kiosk.mysimplelivedatamitviewmodel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()


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

        setNumberToTextView()

        // Button Click
        binding.button.setOnClickListener {
            mainViewModel.addNumber()
            setNumberToTextView()
        }


        // LiveData
        mainViewModel.squareValue.observe(this, {
            binding.tvKaresi.text = "$it"
           // binding.tvKaresi.text = it.toString()

        })

    }


    private fun setNumberToTextView() {
        //binding.tvSayi.text = mainViewModel.number.toString()
        //Sayiyi (number) göster ve textview'e yaz
        mainViewModel.number.toString().also { binding.tvSayi.text = it }
    }

}