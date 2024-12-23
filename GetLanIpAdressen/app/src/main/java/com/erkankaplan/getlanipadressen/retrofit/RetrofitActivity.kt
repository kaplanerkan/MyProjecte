package com.erkankaplan.getlanipadressen.retrofit

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.erkankaplan.getlanipadressen.R
import com.erkankaplan.getlanipadressen.adapter.ProductGroupAdapter
import com.erkankaplan.getlanipadressen.databinding.ActivityRetrofitBinding
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel
import com.erkankaplan.getlanipadressen.room._05_viewmodels.IngredientViewModel
import com.erkankaplan.getlanipadressen.room._05_viewmodels.ProductViewModel
import com.erkankaplan.getlanipadressen.room._05_viewmodels.ViewModelUserModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RetrofitActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRetrofitBinding

    private val productViewModel : ProductViewModel by viewModels()
    private val userViewModel : ViewModelUserModel by viewModels()
    private val ingredientViewModel: IngredientViewModel by viewModels()

    private var resultMessage : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //setContentView(R.layout.activity_retrofit)
        binding = ActivityRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

    }


    private fun initViews() {
        Toasty.Config.getInstance().tintIcon(true) // optional (apply textColor also to the icon)
            .setTextSize(18) // optional
            .allowQueue(true) // optional (prevents several Toastys from queuing)
            .setGravity(Gravity.CENTER) // optional (set toast gravity, offsets are optional)
            .apply() // required

        fetchDbOlaylari()
        userOlaylari()

        soldakiGruplariDoldur()
    }



    /////////////////  Produkt Gruplari  /////////////////

    private lateinit var adapterProductGroup : ProductGroupAdapter
    private fun soldakiGruplariDoldur() {
        adapterProductGroup = ProductGroupAdapter()
        binding.rvProductGroups.adapter = adapterProductGroup

       // binding.rvProductGroups.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val spanCount = 1 // Bir satırda kaç öğe olacağını belirler.
        binding.rvProductGroups.layoutManager =  GridLayoutManager(this, spanCount, GridLayoutManager.HORIZONTAL, false)



        productViewModel.allProductGroups.observe(this) { groups ->
            adapterProductGroup.submitList(groups)
        }

        adapterProductGroup.setOnItemClickListener(object : ProductGroupAdapter.OnItemClickListener {
            override fun onItemClick(productGroup : ProductGroupModel) {
                Log.e("ProductGroupAdapter", "onItemClick:${productGroup.id}  ${productGroup.name}")
                //Toasty.info(this@RetrofitActivity, "ProductGroup: ${productGroup.name}", Toast.LENGTH_SHORT).show()
            }
        })


        //adapteri doldur
        adapterProductGroup.submitList(productViewModel.allProductGroups.value)


        // Click listener for RecyclerView items: Produkt Gruplari
        adapterProductGroup.itemClickOnProductGroupsLiveData.observe(this) { productGroup ->
            handleItemClick(productGroup)
        }

    }

    // Click listener for RecyclerView items: Produkt Gruplari
    private fun handleItemClick(productGroup: ProductGroupModel) {
        //Toast.makeText(this, "Item clicked: ${productGroup.name}", Toast.LENGTH_SHORT).show()
        Log.e("ProductGroupAdapter", "handleItemClick:${productGroup.id}  ${productGroup.name}")
        productViewModel.getProductByGroupId(productGroup.id).observe(this) { it ->
            it.forEach {products ->
                Log.e("ProductGroupAdapter", "handleItemClick: ${products.name}")
            }

        }
    }

    /////////////////  Produkt Gruplari  /////////////////




    private fun fetchDbOlaylari() {
        setProductObservers()
        setIngredientObservers()

        binding.btnFetchProdukte.setOnClickListener {
            // UI Thread üzerinde çalıştığımız için Main Dispatchers kullanıyoruz.
            binding.progressBar.visibility = View.VISIBLE
            binding.tvLutfenBekleyin.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiResponse = RetrofitInstance.getInstance().getApiResponse()

                    productViewModel.fetchProductGroupsFromApi(apiResponse)
                    productViewModel.fetchProductIngredientLimitsFromApi(apiResponse)
                    productViewModel.fetchProductIngredientsFromApi(apiResponse)
                    productViewModel.fetchProductsFromApi(apiResponse)
                    productViewModel.fetchProductPriceFromApi(apiResponse)

                    ingredientViewModel.fetchIngredientsFromApi(apiResponse)
                    ingredientViewModel.fetchIngredientsGroupFromApi(apiResponse)
                    ingredientViewModel.fetchIngredientsPriceFromApi(apiResponse)

                    userViewModel.fetchUserModelFromApi(apiResponse)

                } catch (e : Exception) {
                    e.printStackTrace()
                } finally {
                    // İşlem bittiğinde progress bar ve metni gizle
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        binding.tvLutfenBekleyin.visibility = View.GONE
                    }
                }
            }
        }


    }


    private fun setIngredientObservers() {

        /** All Ingredients */
        ingredientViewModel.allIngredients.observe(this) { ingredients ->
            // UI'ı güncelleyin, örneğin RecyclerView'a verileri ekleyin
            // recyclerView.adapter.submitList(products)

//            ingredients.forEach {
//                Log.e("ProductViewModel", "allIngredients: ${it.name}")
//            }
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allIngredients: ${ingredients.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allIngredients: ${ingredients.size}\n"
            }
        }


        ingredientViewModel.allIngredientGroups.observe(this) { groups ->
            // UI'ı güncelleyin, örneğin RecyclerView'a verileri ekleyin
            // recyclerView.adapter.submitList(products)
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allIngredientGroups: ${groups.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allIngredientGroups: ${groups.size}\n"
            }
        }

        ingredientViewModel.allIngredientPrices.observe(this) { prices ->
            // UI'ı güncelleyin, örneğin RecyclerView'a verileri ekleyin
            // recyclerView.adapter.submitList(products)
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allIngredientPrices: ${prices.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allIngredientPrices: ${prices.size}\n"
            }
        }

    }
    private fun setProductObservers() {
        // Verileri almak için gözlemliyor
        productViewModel.allProducts.observe(this) { products ->
            // UI'ı güncelleyin, örneğin RecyclerView'a verileri ekleyin
            // recyclerView.adapter.submitList(products)

            products.forEach {
                Log.e("ProductViewModel", "allProducts: ${it.name}")
            }
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allProducts: ${products.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allProducts: ${products.size}\n"
            }
        }

        productViewModel.allProductGroups.observe(this) { groups ->
            // UI'ı güncelleyin, örneğin RecyclerView'a verileri ekleyin
            // recyclerView.adapter.submitList(products)

//            groups.forEach {
//                Log.e("ProductViewModel", "allProducts: ${it.name}")
//            }
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allProductGroups: ${groups.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allProductGroups: ${groups.size}\n"
            }
        }

        productViewModel.allProductIngredientLimits.observe(this) { limits ->
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allProductIngredientLimits: ${limits.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allProductIngredientLimits: ${limits.size}\n"
            }
        }
        productViewModel.allProductIngredients.observe(this) { ingredients ->
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allProductIngredients: ${ingredients.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allProductIngredients: ${ingredients.size}\n"
            }
        }
        productViewModel.allProductPrices.observe(this) { prices ->
            // UI'ı güncelleyin, örneğin RecyclerView'a verileri ekleyin
            // recyclerView.adapter.submitList(products)
            runOnUiThread {
//                Toasty.info(this@RetrofitActivity,
//                            "allProductPrices: ${prices.size}",
//                            Toast.LENGTH_SHORT).show()
                resultMessage += "allProductPrices: ${prices.size}\n"
            }
        }

    }


    private fun userOlaylari() {

        // Userler eklendiginde burası çalışıyor
        userViewModel.allUsers.observe(this@RetrofitActivity) { it ->
            //Log.e("ProductViewModel", "eklenenUserler: $it")
            it.forEach {
                Log.e("ProductViewModel", "initViews: ${it.userName}")
            }
        }

        // Userler silindiginde burası çalışıyor
        userViewModel.deleteResult.observe(this) { result ->
            //result.forEach
            if (result != null) {
                if (result > 0) {
                    Toasty.success(this, "$result users deleted", Toast.LENGTH_SHORT, true).show()
                } else if (result == 0) {
                    Toasty.info(this, "No users to delete", Toast.LENGTH_SHORT, true).show()
                } else {
                    Toasty.error(this, "Failed to delete users", Toast.LENGTH_SHORT, true).show()
                }
            }
        }

        // User sil butonuna basıldıgında
        binding.btnDeleteUsers.setOnClickListener {
            userViewModel.deleteAllUsers()
        }


    }


}