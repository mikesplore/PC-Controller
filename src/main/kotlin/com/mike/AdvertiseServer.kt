package com.mike

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("AdvertiseServer")

@OptIn(DelicateCoroutinesApi::class)
fun broadcastServerAvailability(port: Int = 8080) {
    try {
        val broadcastAddress = InetAddress.getByName("255.255.255.255")
        val socket = DatagramSocket().apply {
            broadcast = true
        }

        val pcName = InetAddress.getLocalHost().hostName

        // Get MAC address safely
        val macAddress = getMacAddress()?.joinToString(":") { "%02X".format(it) } ?: "UNKNOWN"
        val serverInfo = "SERVER_DISCOVERY:$pcName:$macAddress:localhost:$port"
        val buffer = serverInfo.toByteArray()

        val packet = DatagramPacket(buffer, buffer.size, broadcastAddress, 9999)

        GlobalScope.launch {
            while (true) {
                socket.send(packet)
                logger.info("Broadcast server availability: $serverInfo")
                delay(5000) // Broadcast every 5 seconds
            }
        }
    } catch (e: Exception) {
        logger.error("Error broadcasting server availability", e)
    }
}

private fun getMacAddress(): ByteArray? {
    return NetworkInterface.getNetworkInterfaces().toList()
        .firstOrNull { it.inetAddresses.hasMoreElements() && !it.isLoopback && it.hardwareAddress != null }
        ?.hardwareAddress
}