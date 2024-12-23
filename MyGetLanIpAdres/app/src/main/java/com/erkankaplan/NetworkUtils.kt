package com.erkankaplan

import java.io.BufferedReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException


object NetworkUtils {

    fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address.address.size == 4) { // IPv4 kontrolü
                        return address.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return null
    }

    fun getMacAddress(ipAddress: String): String? {
        try {
            val inetAddress = InetAddress.getByName(ipAddress)
            val networkInterface = NetworkInterface.getByInetAddress(inetAddress)
            if (networkInterface != null) {
                val macBytes = networkInterface.hardwareAddress
                if (macBytes != null) {
                    val stringBuilder = StringBuilder()
                    for (i in macBytes.indices) {
                        stringBuilder.append(String.format("%02X:", macBytes[i]))
                    }

                    if (stringBuilder.isNotEmpty()) {
                        stringBuilder.deleteCharAt(stringBuilder.length - 1)
                    }
                    return stringBuilder.toString()

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getConnectedDevices(): Map<String, String?> {
        val devices = mutableMapOf<String, String?>()
        try {
            val localIp = getLocalIpAddress()
            if (localIp != null) {
                val prefix = localIp.substring(0, localIp.lastIndexOf(".") + 1) //Ağ prefixini al 192.168.1. gibi
                for (i in 1..254) {
                    val ip = prefix + i
                    try{
                        val address = InetAddress.getByName(ip)
                        if (address.isReachable(50)) { //50ms timeout ile ping at
                            val mac = getMacAddress(ip)
                            devices[ip] = mac
                        }
                    }
                    catch (e:Exception){
                        //Host bulunamadığında veya başka bir hata durumunda devam et.
                    }

                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

        return devices
    }


    fun getMacFromArpTable(ip: String): String? {
        try {
            val process = ProcessBuilder("ip", "-s", "neigh").start() // Linux'te arp -a komutu yerine
            val reader = BufferedReader(process.inputStream.reader())
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.contains(ip)) {
                    val parts = line!!.split("\\s+".toRegex()) // Boşluklara göre ayır
                    if(parts.size > 4) //satır formatının doğru olduğundan emin oluyoruz.
                        return parts[4] // MAC adresi genellikle 5. sütunda olur.
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}