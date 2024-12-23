package com.erkankaplan.ui

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.erkankaplan.ConstObjecte.REQUEST_WIFI_STATE_PERMISSION
import com.erkankaplan.NetworkUtils
import com.erkankaplan.R
import com.erkankaplan.adapter.IpAdreslerAdapter
import com.erkankaplan.adapter.IpAdreslerModel
import com.erkankaplan.adapter.OnItemClickListener
import com.erkankaplan.databinding.ActivityMainBinding
import com.erkankaplan.viewmodels.IpsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Collections
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var ipsAdapter : IpAdreslerAdapter
    private val ipsViewModel: IpsViewModel by viewModels() // ViewModel'ı almak için
    private var list : List<String> = emptyList()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

    }

    private fun initViews(){
        checkSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
        val sonuc = checkWifiStatePermission()



        //binding.rvIpAdresleri.layoutManager = LinearLayoutManager(this)
        //val gridLayoutManager = GridLayoutManager(this, 6, GridLayoutManager.HORIZONTAL, false)
        //val gridLayoutManager2 = GridLayoutManager(this, 2) // 2 sütunlu ızgara
        val horizontalLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        binding.rvIpAdresleri.layoutManager = horizontalLayoutManager

        // Başlangıçta boş bir liste ile adapter'ı oluşturuyoruz

        ipsAdapter = IpAdreslerAdapter(mutableListOf(), object : OnItemClickListener {
            override fun onItemClick(ips : IpAdreslerModel) {
                // Tıklanan öğeyi işleme
                Log.e("IP", ips.ipAdres)
            }
        })

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

//
//        getMyIpMacPairs().forEach { (ipAddress, macAddress) ->
//            Log.e("IP-MAC", "IP: $ipAddress, MAC: $macAddress")
//        }

//        getMyMacAddresses().forEach { macAddress ->
//            Log.e("MAC", "MAC2: $macAddress")
//        }


//        getMyMacAddresses2().forEach { (macAddress, ipAddress) ->
//            Log.e("MAC-IP", "MAC: $macAddress, IP: $ipAddress")
//        }


//        getConnectedDevicesEnhanced().forEach { (ipAddress, macAddress) ->
//            Log.e("IP-MAC", "IP: $ipAddress, MAC: $macAddress")
//        }


//        val ipAddress = "192.168.1.177" // Test için bir IP adresi
//        val macAddress = NetworkUtils.getMacFromArpTable(ipAddress)
//        if(macAddress != null){
//            println("ARP Tablosundan MAC Adresi: $macAddress")
//        }else{
//            println("ARP tablosundan MAC adresi okunamadı. Erişim kısıtlı olabilir.")
//        }


//        val localAgdaBagliCihazlar = getLocalNetworkIPs()
//        for (cihaz in localAgdaBagliCihazlar) {
//            println("IP: $cihaz")
//        }

//        getLocalNetworkIPs().forEach { ip ->
//            Log.e("IP", ip)
//        }


    }




    @OptIn(DelicateCoroutinesApi::class)
    private fun getLocalNetworkIPs() : List<String> {
        val ipList = mutableListOf<String>()
        val myIp = getWifiIpAddress()  // InetAddress.getLocalHost().hostAddress
        val subnet = myIp?.substringBeforeLast(".")  // Cihazınızın IP'sini alıp subneti buluyoruz.

        for (i in 1..254) {  // Subnet'teki tüm IP'leri tarıyoruz (1-254 arası)
            val host = "$subnet.$i"
            GlobalScope.launch {
                try {
                    val inet = InetAddress.getByName(host)
                    //val macAdress = InetAddress.getLocalHost().hostAddress
                    val macAdress = NetworkUtils.getMacFromArpTable(host)
                    if (inet.isReachable(100)) {  // 100ms ping timeout süresi
                        ipList.add(host)
                        //Log.e("IP", host)
                        //ipsAdapter.addUrun(IpAdreslerModel(host, macAdress?:""))
                        runOnUiThread {
                            ipsViewModel.addIp(IpAdreslerModel(host, macAdress ?:""))
                        }

                    }
                } catch (e : Exception) {
                    Log.e("IP", "Error getting IP address ::", e)
                    // Erişilemeyen IP adresi
                }
            }
        }
        return ipList
    }


    private fun getWifiIpAddress() : String? {

        // val ipList = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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



    private fun getWifiIpAddress2() : String {
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
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






    private fun checkWifiStatePermission() : Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(this@MainActivity,
                                                                android.Manifest.permission.ACCESS_WIFI_STATE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Eğer izin verilmemişse, kullanıcıdan izni isteyelim.
            ActivityCompat.requestPermissions(this@MainActivity,
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
                Log.e("PERMISSION", "Permission granted")
            } else {
                // İzin reddedildi, kullanıcıya bildirin
                Log.e("PERMISSION", "Permission denied")
            }
        }
    }

}