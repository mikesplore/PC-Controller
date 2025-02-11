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
        // Use the broadcast address for Linux
        val broadcastAddress = InetAddress.getByName("255.255.255.255")
        val socket = DatagramSocket().apply {
            broadcast = true // Enable broadcasting
        }

        // Get the hostname of the Linux machine
        val pcName = InetAddress.getLocalHost().hostName

        // Get the MAC address of the primary network interface
        val macAddress = getMacAddress()?.joinToString(":") { "%02X".format(it) } ?: "UNKNOWN"

        // Get the type of operating system
        val os = System.getProperty("os.name")

        // Prepare the server information message
        val serverInfo = "SERVER_DISCOVERY:$pcName:$macAddress:localhost:$port:$os"
        val buffer = serverInfo.toByteArray()

        // Create a UDP packet to broadcast
        val packet = DatagramPacket(buffer, buffer.size, broadcastAddress, 9999)

        // Launch a coroutine to broadcast the server information periodically
        GlobalScope.launch {
            while (true) {
                socket.send(packet)
                //logger.info("Broadcast server availability: $serverInfo")
                delay(5000) // Broadcast every 5 seconds
            }
        }
    } catch (e: Exception) {
        logger.error("Error broadcasting server availability", e)
    }
}

/**
 * Retrieves the MAC address of the primary network interface on Linux.
 */
private fun getMacAddress(): ByteArray? {
    return try {
        // Iterate through all network interfaces
        NetworkInterface.getNetworkInterfaces().toList()
            .asSequence()
            .filter { !it.isLoopback && it.isUp } // Filter out loopback and inactive interfaces
            .filter { it.hardwareAddress != null } // Ensure the interface has a MAC address
            .firstOrNull() // Get the first valid interface
            ?.hardwareAddress // Retrieve the MAC address
    } catch (e: Exception) {
        logger.error("Error retrieving MAC address", e)
        null
    }
}