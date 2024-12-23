package com.erkankaplan

class Mull {



//    fun getConnectedDevicesEnhanced(): Map<String, String?> {
//        val devices = mutableMapOf<String, String?>()
//        try {
//            val localIp = getLocalIpAddress()
//            if (localIp != null) {
//                val prefix = localIp.substring(0, localIp.lastIndexOf(".") + 1)
//                for (i in 1..254) {
//                    val ip = prefix + i
//                    try{
//                        val address = InetAddress.getByName(ip)
//                        if (address.isReachable(50)) {
//                            val mac = getMacAddress(ip)
//                            if(mac != null){ //Sadece MAC adresi bulunan cihazları ekle
//                                devices[ip] = mac
//                            }
//                        }
//                    }
//                    catch (e:Exception){
//                        //Host bulunamadığında veya başka bir hata durumunda devam et.
//                    }
//
//                }
//            }
//
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//
//        return devices
//    }

//    private fun getLocalIpAddress(): String? {
//        try {
//            val interfaces = NetworkInterface.getNetworkInterfaces()
//            while (interfaces.hasMoreElements()) {
//                val networkInterface = interfaces.nextElement()
//                val addresses = networkInterface.inetAddresses
//                while (addresses.hasMoreElements()) {
//                    val address = addresses.nextElement()
//                    if (!address.isLoopbackAddress && address.address.size == 4) { // IPv4 kontrolü
//                        return address.hostAddress
//                    }
//                }
//            }
//        } catch (e: SocketException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//



//
//    private fun getMacAddress(ipAddress: String): String? {
//        return try {
//            // Hedef IP için InetAddress oluştur
//            val ip = InetAddress.getByName(ipAddress) ?: return null
//
//            // IP adresine ait NetworkInterface'i al
//            val networkInterface = NetworkInterface.getByInetAddress(ip) ?: return null
//
//            // MAC adresini byte dizisi olarak al
//            val macBytes = networkInterface.hardwareAddress ?: return null
//
//            // MAC adresini okunabilir formata dönüştür
//            macBytes.joinToString(":") { byte -> String.format("%02X", byte) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null // Hata durumunda null döndür
//        }
//    }
//
//
//
//
//
//    private fun getMyIpMacPairs(): List<Pair<String, String>> {
//        val ipMacList = mutableListOf<Pair<String, String>>()
//        try {
//            val interfaces = NetworkInterface.getNetworkInterfaces()
//            while (interfaces.hasMoreElements()) {
//                val networkInterface = interfaces.nextElement()
//                val macBytes = networkInterface.hardwareAddress
//                if (macBytes != null) {
//                    val sb = StringBuilder()
//                    for (b in macBytes) {
//                        sb.append(String.format("%02X:", b))
//                    }
//                    if (sb.isNotEmpty()) {
//                        sb.deleteCharAt(sb.length - 1) // Son ':' karakterini sil
//                        val macAddress = sb.toString()
//
//                        // Arayüzün IP adreslerini al
//                        val addresses = networkInterface.inetAddresses
//                        while (addresses.hasMoreElements()) {
//                            val address = addresses.nextElement()
//                            if (!address.isLoopbackAddress && address is Inet4Address) {
//                                val ipAddress = address.hostAddress
//                                ipMacList.add(Pair(ipAddress, macAddress))
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return ipMacList
//    }
//
//
//
//
//
//    private fun getMyMacAddresses(): List<String> {
//        val macAddressList = mutableListOf<String>()
//        try {
//            val interfaces = NetworkInterface.getNetworkInterfaces()
//            while (interfaces.hasMoreElements()) {
//                val networkInterface = interfaces.nextElement()
//                val macBytes = networkInterface.hardwareAddress
//                if (macBytes != null) {
//                    val sb = StringBuilder()
//                    for (b in macBytes) {
//                        sb.append(String.format("%02X:", b))
//                    }
//                    if (sb.isNotEmpty()) {
//                        sb.deleteCharAt(sb.length - 1) // Son ':' karakterini sil
//                        macAddressList.add(sb.toString())
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return macAddressList
//    }
//
//
//    private fun getMyMacAddresses2(): List<Pair<String, String>> {
//        val macAddressList = mutableListOf<Pair<String, String>>()
//        try {
//            val interfaces = NetworkInterface.getNetworkInterfaces()
//            while (interfaces.hasMoreElements()) {
//                val networkInterface = interfaces.nextElement()
//                val macBytes = networkInterface.hardwareAddress
//                if (macBytes != null && macBytes.isNotEmpty() && !macBytes.all { it == 0.toByte() }) { // Geçersiz MAC adreslerini filtrele
//                    val sb = StringBuilder()
//                    for (b in macBytes) {
//                        sb.append(String.format("%02X:", b))
//                    }
//                    if (sb.isNotEmpty()) {
//                        sb.deleteCharAt(sb.length - 1) // Son ':' karakterini sil
//                        val macAddress = sb.toString()
//
//                        // IP adreslerini al
//                        val inetAddresses = networkInterface.inetAddresses
//                        while (inetAddresses.hasMoreElements()) {
//                            val inetAddress = inetAddresses.nextElement()
//                            if (!inetAddress.isLoopbackAddress) {
//                                macAddressList.add(Pair(macAddress, inetAddress.hostAddress))
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return macAddressList
//    }




}