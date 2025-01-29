package com.mike.model

import java.util.*


data class OperatingSystemInfo(
    val name: String,
    val version: String,
    val architecture: String,
    val manufacturer: String,
    val buildNumber: String,
    val installDate: Long?
)


data class ComputerSystemDetails(
    val manufacturer: String,
    val model: String,
    val serialNumber: String
)

data class ProcessorDetails(
    val name: String,
    val physicalCoreCount: Int,
    val logicalCoreCount: Int,
    val maxFrequency: Long,
    val currentFrequency: LongArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProcessorDetails

        if (name != other.name) return false
        if (physicalCoreCount != other.physicalCoreCount) return false
        if (logicalCoreCount != other.logicalCoreCount) return false
        if (maxFrequency != other.maxFrequency) return false
        if (!currentFrequency.contentEquals(other.currentFrequency)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + physicalCoreCount
        result = 31 * result + logicalCoreCount
        result = 31 * result + maxFrequency.hashCode()
        result = 31 * result + currentFrequency.contentHashCode()
        return result
    }
}

data class MemoryDetails(
    val total: Long,
    val available: Long,
    val used: Long,
    val cacheSize: Long,
    val swapTotal: Long,
    val swapUsed: Long
)

data class BatteryDetails(
    val name: String,
    val remainingCapacity: Double,
    val timeRemaining: Long,
    val voltage: Double,
    val temperature: Double,
    val isCharging: Boolean,
    val cycleCount: Int,
    val designCapacity: Int,
    val fullChargeCapacity: Int,
    val batteryHealth: Double,
    val batteryChemistry: String,
    val batteryStatus: String,
    val manufactureDate: Any,
    val serialNumber: String
)

data class DisplayInfo(
    val deviceName: String,
    val width: Int,
    val height: Int,
    val refreshRate: Int
)

data class SoftwareInfo(
    val runningProcessCount: Int,
    val systemUptime: Long,
    val jvmDetails: JvmDetails,
    val environmentVariables: Map<String, String>
)

data class JvmDetails(
    val version: String,
    val vendor: String,
    val home: String,
    val availableProcessors: Int,
    val maxMemory: Long
)


data class NetworkInterfaceDetails(
    val name: String,
    val displayName: String,
    val ipv4Address: List<String>,
    val ipv6Address: List<String>,
    val macAddress: String,
    val mtu: Int,
    val isUp: Boolean,
    val isVirtual: Boolean
)


data class DetailedProcessInfo(
    val pid: Int,
    val name: String,
    val cpuUsage: Double,
    val memoryUsage: Long,
    val startTime: Long,
    val path: String
)

data class StorageInfo(
    val mountPoints: List<MountPointDetails>,
    val totalSpace: Long,
    val usedSpace: Long,
    val freeSpace: Long
)

data class MountPointDetails(
    val mountPoint: String,
    val type: String,
    val description: String,
    val total: Long,
    val used: Long,
    val available: Long,
    val usagePercentage: Double
)

data class UserEnvironmentInfo(
    val username: String,
    val homeDirectory: String,
    val language: String,
    val country: String,
    val timezone: String,
    val locale: Locale
)

data class FileTypeUsage(
    val type: String,
    val count: Long,
    val totalSize: Long
)



