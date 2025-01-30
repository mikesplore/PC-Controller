package com.mike.serverFunctions

import com.mike.model.*
import kotlinx.io.IOException
import oshi.SystemInfo
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem
import java.awt.GraphicsEnvironment
import java.io.File
import java.net.NetworkInterface
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*


class SystemInformationCollector {
    private val systemInfo = SystemInfo()
    private val hal: HardwareAbstractionLayer = systemInfo.hardware
    private val os: OperatingSystem = systemInfo.operatingSystem

    fun collectOperatingSystemInfo(): OperatingSystemInfo {
        return OperatingSystemInfo(
            name = os.family,
            version = os.versionInfo.version,
            architecture = os.bitness.toString(),
            manufacturer = os.manufacturer,
            buildNumber = os.versionInfo.buildNumber ?: "Unknown",
            installDate = os.systemBootTime
        )
    }



    fun collectComputerSystemDetails(): ComputerSystemDetails {
        val computerSystem = hal.computerSystem
        return ComputerSystemDetails(
            manufacturer = computerSystem.manufacturer,
            model = computerSystem.model,
            serialNumber = computerSystem.serialNumber
        )
    }

    fun collectProcessorDetails(): ProcessorDetails {
        val processor = hal.processor
        return ProcessorDetails(
            name = processor.processorIdentifier.name,
            physicalCoreCount = processor.physicalProcessorCount,
            logicalCoreCount = processor.logicalProcessorCount,
            maxFrequency = processor.maxFreq,
            currentFrequency = processor.currentFreq
        )
    }

    fun collectMemoryDetails(): MemoryDetails {
        val memory = hal.memory
        return MemoryDetails(
            total = memory.total,
            available = memory.available,
            used = memory.total - memory.available,
            cacheSize = memory.physicalMemory.size.toLong(),
            swapTotal = memory.virtualMemory.swapTotal,
            swapUsed = memory.virtualMemory.swapUsed
        )
    }

    fun collectBatteryDetails(): BatteryDetails? {
        val powerSources = hal.powerSources
        return powerSources.firstOrNull()?.let { battery ->
            BatteryDetails(
                name = battery.name,
                remainingCapacity = battery.remainingCapacityPercent * 100,
                timeRemaining = battery.timeRemainingEstimated.toLong(),
                voltage = battery.voltage,
                temperature = (battery.temperature.takeIf { it > 0 } ?: -1).toDouble(),
                isCharging = battery.powerUsageRate > 0,
                cycleCount = battery.cycleCount,
                designCapacity = battery.designCapacity,
                fullChargeCapacity = battery.maxCapacity,
                batteryHealth = if (battery.maxCapacity > 0) {
                    ((battery.maxCapacity.toDouble() / battery.designCapacity) * 100).coerceIn(0.0, 100.0)
                } else {
                    0.0
                },
                batteryChemistry = battery.chemistry,
                batteryStatus = battery.serialNumber,
                manufactureDate = battery.manufactureDate ?: "Unknown",
                serialNumber = battery.serialNumber
            )
        }
    }


    fun collectDisplayInfo(): List<DisplayInfo> {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gs = ge.screenDevices
        return gs.mapIndexed { index, gd ->
            val dm = gd.displayMode
            DisplayInfo(
                deviceName = "Display ${index + 1}",
                width = dm.width,
                height = dm.height,
                refreshRate = dm.refreshRate
            )
        }
    }

    fun collectSoftwareInfo(): SoftwareInfo {
        val processes = os.processes
        return SoftwareInfo(
            runningProcessCount = processes.size,
            systemUptime = os.systemUptime,
        )
    }

    fun collectJvmDetails(): JvmDetails {
        val runtime = Runtime.getRuntime()
        return JvmDetails(
            version = System.getProperty("java.version"),
            vendor = System.getProperty("java.vendor"),
            home = System.getProperty("java.home"),
            availableProcessors = runtime.availableProcessors(),
            maxMemory = runtime.maxMemory()
        )
    }



    fun collectStorageInfo(): StorageInfo {
        val roots = File.listRoots()
        return StorageInfo(
            mountPoints = roots.map { root ->
                MountPointDetails(
                    mountPoint = root.absolutePath,
                    type = Files.getFileStore(Paths.get(root.absolutePath)).type(),
                    description = root.absolutePath,
                    total = root.totalSpace,
                    used = root.totalSpace - root.freeSpace,
                    available = root.freeSpace,
                    usagePercentage = (root.totalSpace - root.freeSpace).toDouble() / root.totalSpace * 100
                )
            },
            totalSpace = roots.sumOf { it.totalSpace },
            usedSpace = roots.sumOf { it.totalSpace - it.freeSpace },
            freeSpace = roots.sumOf { it.freeSpace }
        )
    }

    fun collectUserEnvironmentInfo(): UserEnvironmentInfo {
        return UserEnvironmentInfo(
            username = System.getProperty("user.name"),
            homeDirectory = System.getProperty("user.home"),
            language = Locale.getDefault().language,
            country = Locale.getDefault().country,
            timezone = TimeZone.getDefault().id,
            locale = Locale.getDefault()
        )
    }
}