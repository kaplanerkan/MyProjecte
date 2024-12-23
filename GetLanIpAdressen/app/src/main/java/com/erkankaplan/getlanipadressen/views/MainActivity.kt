package com.erkankaplan.getlanipadressen.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.erkankaplan.getlanipadressen.R
import com.erkankaplan.getlanipadressen.adapter.IpAdreslerAdapter
import com.erkankaplan.getlanipadressen.databinding.ActivityMainBinding
import com.erkankaplan.getlanipadressen.models.IpAdreslerModel
import com.erkankaplan.getlanipadressen.retrofit.RetrofitActivity
import com.erkankaplan.getlanipadressen.room.UserAktivity
import com.erkankaplan.getlanipadressen.tools.UIUtils
import com.erkankaplan.getlanipadressen.viewmodels.IpsViewModel
import com.erkankaplan.getlanipadressen.views.kitaplar.KitaplarActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale

class MainActivity : AppCompatActivity(),
                     IpAdreslerAdapter.OnItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private var list : List<String> = emptyList()
    private lateinit var ipsAdapter : IpAdreslerAdapter
    private val ipsViewModel: IpsViewModel by viewModels() // ViewModel'ı almak için


    companion object {
        private const val REQUEST_WIFI_STATE_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

    }


    @OptIn(DelicateCoroutinesApi::class) private fun initViews() {

        checkSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
        val sonuc = checkWifiStatePermission(this)

        binding.btnSearch.setOnClickListener {
            GlobalScope.launch {
                //val ipList = getLocalNetworkIPs()
                // val ipList = getWifiIpAddress(this@MainActivity)

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(this@MainActivity,
                                               android.R.layout.simple_list_item_1,
                                               list).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }

                    // `notifyDataSetChanged()` çağrısı, yeni `adapter` oluşturulduğunda gerekli değildir,
                    // çünkü `adapter` zaten yeni verilerle birlikte oluşturulmuştur.
                    // Fakat eğer var olan bir `adapter` ile çalışıyorsanız, güncelleme sonrasında çağırmak gerekir.

                    binding.spinner.adapter = adapter
                    // Eğer var olan bir `adapter`'ı güncelleyip tekrar kullanıyorsanız, bu satırı açabilirsiniz:
                    adapter.notifyDataSetChanged()

                    // binding.spinner.setSelection(0) // Opsiyonel, ilk öğeyi seçmek isterseniz
                }
            }
        }

        binding.btnShowInPopUp.setOnClickListener {
            showPopUp()
        }
        binding.btnFetchProducts.setOnClickListener {
            val intent = Intent(this, RetrofitActivity::class.java)
            startActivity(intent)
        }
        binding.btnKitaplar.setOnClickListener {
            val intent = Intent(this, KitaplarActivity::class.java)
            startActivity(intent)
        }


        //binding.rvIpAdresleri.layoutManager = LinearLayoutManager(this)
        //val gridLayoutManager = GridLayoutManager(this, 6, GridLayoutManager.HORIZONTAL, false)
        //val gridLayoutManager2 = GridLayoutManager(this, 2) // 2 sütunlu ızgara
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvIpAdresleri.layoutManager = horizontalLayoutManager

        // Başlangıçta boş bir liste ile adapter'ı oluşturuyoruz
        ipsAdapter = IpAdreslerAdapter(mutableListOf(), this)
        binding.rvIpAdresleri.adapter = ipsAdapter


        // ViewModel'deki LiveData'ı gözlemleme
        ipsViewModel.ipAdreslerList.observe(this) {
            ipsAdapter.addUrun(it)
        }




        // Örnek olarak veri ekleme
        //ipsAdapter.addUrun(IpAdreslerModel("Ürün 1", "Depo 1"))
        //ipsAdapter.addUrun(IpAdreslerModel("Ürün 2", "Depo 2"))

        //ipsViewModel.addIp(IpAdreslerModel("host", "test"))
        list = getLocalNetworkIPs()



        setStartUser()

    }

    private fun setStartUser() {
        binding.btnStartUser.setOnClickListener {
            val intent = Intent(this, UserAktivity::class.java)
            startActivity(intent)
        }

    }


    private fun showPopUp() {

        val listPopupWindow = UIUtils.getPlainListPopUpWindow(context = this,
                                                              items = list,
                                                              anchor = binding.btnShowInPopUp,
                                                              cellLayoutRes = R.layout.layout_filter_option,
                                                              backgroundDrawableRes = R.drawable.bg_tooltip)

        listPopupWindow.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            // Seçilen öğe ile yapılacak işlemler
            Toast.makeText(this, "Selected item: $selectedItem", Toast.LENGTH_SHORT).show()
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
    }


    @OptIn(DelicateCoroutinesApi::class) private fun getLocalNetworkIPs() : List<String> {
        val ipList = mutableListOf<String>()
        val myIp = getWifiIpAddress(this)  // InetAddress.getLocalHost().hostAddress
        val subnet = myIp?.substringBeforeLast(".")  // Cihazınızın IP'sini alıp subneti buluyoruz.

        for (i in 1..254) {  // Subnet'teki tüm IP'leri tarıyoruz (1-254 arası)
            val host = "$subnet.$i"
            GlobalScope.launch {
                try {
                    val inet = InetAddress.getByName(host)
                    val macAdress = InetAddress.getLocalHost().hostAddress
                    if (inet.isReachable(100)) {  // 100ms ping timeout süresi
                        ipList.add(host)
                        Log.e("IP", host)
                        //ipsAdapter.addUrun(IpAdreslerModel(host, macAdress?:""))
                        runOnUiThread {
                            ipsViewModel.addIp(IpAdreslerModel(host, macAdress?:""))
                        }

                    }
                } catch (e : Exception) {
                    Log.e("IP", "Erişilemeyen IP adresi")
                    // Erişilemeyen IP adresi
                }
            }
        }
        return ipList
    }


    private fun getWifiIpAddress() : String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress

        // IP adresini integer'dan 192.168.x.x formatına çevirme
        return String.format(Locale.getDefault(),
                             "%d.%d.%d.%d",
                             ipAddress and 0xff,
                             ipAddress shr 8 and 0xff,
                             ipAddress shr 16 and 0xff,
                             ipAddress shr 24 and 0xff)
    }


    private fun getWifiIpAddress(context : Context) : String? {

        // val ipList = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networks = connectivityManager.allNetworks

            for (network in networks) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (networkCapabilities != null && networkCapabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI)) {
                    try {
                        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                        for (networkInterface in Collections.list(networkInterfaces)) {
                            if (!networkInterface.isLoopback && networkInterface.isUp) {
                                val addresses = networkInterface.inetAddresses
                                for (address in Collections.list(addresses)) {
                                    if (!address.isLoopbackAddress && address is InetAddress) {
                                        val ipAddress = address.hostAddress
                                        if (ipAddress != null && ipAddress.contains(".")) {
                                            //ipList.add(ipAddress)
                                            return ipAddress
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ex : Exception) {
                        Log.e("IP", "Error getting IP address", ex)
                    }
                }
            }
        }
//        else {
//            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//            val wifiInfo = wifiManager.connectionInfo
//            val ipAddress = wifiInfo.ipAddress
//
//            val formattedIpAddress = String.format(Locale.getDefault(),
//                                                   "%d.%d.%d.%d",
//                                                   ipAddress and 0xff,
//                                                   ipAddress shr 8 and 0xff,
//                                                   ipAddress shr 16 and 0xff,
//                                                   ipAddress shr 24 and 0xff)
//
//            ipList.add(formattedIpAddress)
//        }
//        return ipList


        return null
    }

    private fun checkWifiStatePermission(context : Context) : Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(context,
                                                                android.Manifest.permission.ACCESS_WIFI_STATE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Eğer izin verilmemişse, kullanıcıdan izni isteyelim.
            ActivityCompat.requestPermissions(context as Activity,
                                              arrayOf(android.Manifest.permission.INTERNET,
                                                      android.Manifest.permission.ACCESS_WIFI_STATE),
                                              REQUEST_WIFI_STATE_PERMISSION)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<String>, grantResults : IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WIFI_STATE_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.e("PERMISSION", "İzin verildi")
            } else {
                // İzin reddedildi, kullanıcıya bildirin
                Log.e("PERMISSION", "İzin reddedildi")
                //   showPermissionRationale()
            }
        }
    }

    override fun onItemClick(ips : IpAdreslerModel) {
        // Tıklanan ürünle ne yapılacaksa burada yapılır
        Toast.makeText(this, "Seçilen Ürün: ${ips.ipAdres}", Toast.LENGTH_SHORT).show()
    }


//    private fun showPermissionRationale() {
//        AlertDialog.Builder(this)
//            .setTitle("Wi-Fi Durum İzni Gerekli")
//            .setMessage("Bu uygulamanın Wi-Fi IP adresinizi alabilmesi için Wi-Fi durum iznine ihtiyacı vardır. Lütfen izni verin.")
//            .setPositiveButton("Tamam") { dialog, which ->
////                // Tekrar izin iste
////                ActivityCompat.requestPermissions(
////                    this,
////                    arrayOf(android.Manifest.permission.ACCESS_WIFI_STATE),
////                    REQUEST_WIFI_STATE_PERMISSION
////                )
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//            .setNegativeButton("İptal") { dialog, which ->
//                // Kullanıcı reddederse, burada bir şey yapabilirsiniz
//            }
//            .create()
//            .show()
//    }

}