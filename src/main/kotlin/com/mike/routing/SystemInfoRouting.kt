package com.mike.routing

import com.mike.serverFunctions.SystemInformationCollector
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSystemInfoRouting() {
    routing {
        val systemInfo = SystemInformationCollector()

        get("battery") {
            val batteryDetails = systemInfo.collectBatteryDetails()
            println("Battery details: $batteryDetails")
            if (batteryDetails != null) {
                call.respond(batteryDetails)
            } else {
                call.respondText("Battery information not available", status = HttpStatusCode.NotFound)
            }
        }

        get("displays") {
            val displayInfo = systemInfo.collectDisplayInfo()
            if (displayInfo.isNotEmpty()) {
                call.respond(displayInfo)
            } else {
                call.respondText("Display information not available", status = HttpStatusCode.NotFound)
            }
        }

        get("software") {
            val softwareInfo = systemInfo.collectSoftwareInfo()
            call.respond(softwareInfo)
        }



        get("storage") {
            val storageInfo = systemInfo.collectStorageInfo()
            call.respond(storageInfo)
        }

        get("userEnvironment") {
            val userEnvironmentInfo = systemInfo.collectUserEnvironmentInfo()
            call.respond(userEnvironmentInfo)
        }

        get("operatingSystem") {
            val operatingSystemInfo = systemInfo.collectOperatingSystemInfo()
            call.respond(operatingSystemInfo)
        }

        get("systemInfo") {
            val name = systemInfo.collectComputerSystemDetails()
            call.respond(name)
        }

        get("processorDetails"){
            val processorDetails = systemInfo.collectProcessorDetails()
            call.respond(processorDetails)
        }

        get("memory"){
            val memoryDetails = systemInfo.collectMemoryDetails()
            call.respond(memoryDetails)
        }

        get("JvmDetails"){
            val jvmDetails = systemInfo.collectJvmDetails()
            call.respond(jvmDetails)
        }

        get("detailedStorageUsage") {
            val detailedStorageUsage = systemInfo.collectDetailedStorageUsage()
            call.respond(detailedStorageUsage)
        }

        get("operatingSystemInfo") {
            val operatingSystemDetails = systemInfo.collectOperatingSystemInfo()
            call.respond(operatingSystemDetails)
        }
    }
}