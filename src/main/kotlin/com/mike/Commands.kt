package com.mike

import kotlinx.io.IOException
import org.h2.engine.DbObject.USER
import java.util.*
import java.io.BufferedReader
import java.io.InputStreamReader

fun executeCommand(command: String) {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())
    try {
        when {
            os.contains("win") -> executeWindowsCommand(command)
            os.contains("mac") -> executeMacCommand(command)
            os.contains("nix") || os.contains("nux") -> executeLinuxCommand(command)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun executeWindowsCommand(command: String) {
    val runtime = Runtime.getRuntime()
    when (command) {
        // System commands
        "shutdown" -> runtime.exec("shutdown -s -t 0")
        "logoff" -> runtime.exec("shutdown -l")
        "restart" -> runtime.exec("shutdown -r -t 0")
        "hibernate" -> runtime.exec("shutdown -h")
        "sleep" -> runtime.exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0")
        "lock" -> runtime.exec("rundll32.exe user32.dll,LockWorkStation")

    }
}

private fun executeMacCommand(command: String) {
    val runtime = Runtime.getRuntime()
    when (command) {
        // System commands
        "shutdown" -> runtime.exec("sudo shutdown -h now")
        "restart" -> runtime.exec("sudo shutdown -r now")
        "hibernate" -> runtime.exec("sudo pmset hibernatemode 25 && sudo pmset hibernate now")
        "logoff" -> runtime.exec("sudo pkill -KILL -u $USER")
        "sleep" -> runtime.exec("pmset sleepnow")
        "lock" -> runtime.exec("pmset displaysleepnow")

    }
}



private fun executeLinuxCommand(command: String) {
    val user = System.getProperty("user.name")

    try {
        when (command) {
            // System commands
            "shutdown" -> executeCommand("shutdown -h now", requireRoot = true)
            "restart" -> executeCommand("shutdown -r now", requireRoot = true)
            "hibernate" -> executeCommand("systemctl hibernate", requireRoot = true)
            "logoff" -> executeCommand("pkill -KILL -u $user")
            "sleep" -> executeCommand("systemctl suspend", requireRoot = true)
            "lock" -> executeCommand("dm-tool lock") // For LightDM (common in Kali Linux)
            else -> println("Unknown command: $command")
        }
    } catch (e: Exception) {
        println("Error executing command '$command': ${e.message}")
    }
}

/**
 * Helper function to execute a Linux command and handle its output/errors.
 * @param command The command to execute.
 * @param requireRoot Whether the command requires root privileges.
 */
private fun executeCommand(command: String, requireRoot: Boolean = false) {
    val runtime = Runtime.getRuntime()
    val fullCommand = if (requireRoot) "echo '0' | sudo -S $command" else command

    try {
        val process = runtime.exec(fullCommand)
        val exitCode = process.waitFor()

        // Read command output
        val output = BufferedReader(InputStreamReader(process.inputStream)).readText()
        val error = BufferedReader(InputStreamReader(process.errorStream)).readText()

        if (exitCode == 0) {
            println("Command executed successfully: $fullCommand")
            if (output.isNotEmpty()) println("Output: $output")
        } else {
            println("Command failed with exit code $exitCode: $fullCommand")
            if (error.isNotEmpty()) println("Error: $error")
        }
    } catch (e: Exception) {
        println("Error executing command '$fullCommand': ${e.message}")
    }
}
