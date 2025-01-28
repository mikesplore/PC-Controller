package com.mike

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * Broadcasts the server availability over the network.
 *
 * This function sends a broadcast message containing the server's PC name, MAC address, host, and port
 * to the network's broadcast address. The message is sent every 5 seconds.
 *
 * @param port The port on which the server is running. Defaults to 8080.
 */
@OptIn(DelicateCoroutinesApi::class)
fun broadcastServerAvailability(port: Int = 8080) {
    val broadcastAddress = InetAddress.getByName("255.255.255.255")
    val socket = DatagramSocket().apply {
        broadcast = true
    }

    val pcName = InetAddress.getLocalHost().hostName
    val networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost())
    val macAddress = networkInterface.hardwareAddress.joinToString(":") { "%02X".format(it) }
    val serverInfo = "SERVER_DISCOVERY:$pcName:$macAddress:localhost:$port"
    val buffer = serverInfo.toByteArray()

    val packet = DatagramPacket(buffer, buffer.size, broadcastAddress, 9999)

    GlobalScope.launch {
        while (true) {
            socket.send(packet)
            delay(5000) // Broadcast every 5 seconds
        }
    }
}