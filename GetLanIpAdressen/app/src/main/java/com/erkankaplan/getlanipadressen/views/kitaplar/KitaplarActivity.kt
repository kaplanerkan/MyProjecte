package com.erkankaplan.getlanipadressen.views.kitaplar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.erkankaplan.getlanipadressen.R
import com.erkankaplan.getlanipadressen.databinding.ActivityKitaplarBinding

class KitaplarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityKitaplarBinding

    private val allBooksAdapter by lazy { AllBooksListAdapter() }
    private val bestSellersAdapter by lazy { BestSellersListAdapter() }

    //HeaderAdapter üzerinden 2 nesne oluşturuyoruz 2 başlık için
    private val allBooksHeaderAdapter by lazy { HeaderAdapter("Tüm Kitaplar") }
    private val bestSellersHeaderAdapter by lazy { HeaderAdapter("Çok Satanlar") }

    private var concatAdapter = ConcatAdapter()


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKitaplarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }


    private fun initViews(){
        allBooksAdapter.updateList(BooksData.getAllBooks())
        bestSellersAdapter.updateList(BooksData.getBestSellers())

        //concatAdapter değişkenimize oluşturduğumuz adapterları gösterim sırasına göre dahil ediyoruz.
        concatAdapter = ConcatAdapter(
            bestSellersHeaderAdapter,
            bestSellersAdapter,
            allBooksHeaderAdapter,
            allBooksAdapter
        )

        //concatAdapter'a adapter ekleme
        //concatAdapter.addAdapter(allBooksAdapter)

        //concatAdapter'a index belirterek adapter ekleme
        //concatAdapter.addAdapter(3, allBooksAdapter)

        //concatAdapter'dan adapter silme
        //concatAdapter.removeAdapter(allBooksAdapter)

        //Activity içerisindeki RecyclerView'a oluşturduğumuz concatAdapter'ı yerleştiriyoruz.
        binding.rvConcat.apply {
            layoutManager =
                LinearLayoutManager(this@KitaplarActivity, LinearLayoutManager.VERTICAL, false)
            adapter = concatAdapter
        }
    }


}