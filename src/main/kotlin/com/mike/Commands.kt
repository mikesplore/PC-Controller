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
            else -> throw UnsupportedOperationException("Unsupported operating system: $os")
        }
    } catch (e: IOException) {
        println("Error executing command: ${e.message}")
        e.printStackTrace()
    }
}

private fun executeWindowsCommand(command: String) {
    val runtime = Runtime.getRuntime()
    val cmd = when (command) {
        // System commands
        "shutdown" -> "shutdown -s -t 0"
        "logoff" -> "shutdown -l"
        "restart" -> "shutdown -r -t 0"
        "hibernate" -> "shutdown -h"
        "sleep" -> "rundll32.exe powrprof.dll,SetSuspendState 0,1,0"
        "lock" -> "rundll32.exe user32.dll,LockWorkStation"
        // Additional operations
        "openNotepad" -> "notepad"
        "openCalculator" -> "calc"
        "openTaskManager" -> "taskmgr"
        "openFileExplorer" -> "explorer"
        "openControlPanel" -> "control"
        "openCmd" -> "cmd /c start cmd"
        else -> throw IllegalArgumentException("Unknown Windows command: $command")
    }
    executeWithLogging(cmd)
}

private fun executeMacCommand(command: String) {
    val runtime = Runtime.getRuntime()
    val (cmd, needsRoot) = when (command) {
        // System commands
        "shutdown" -> Pair("shutdown -h now", true)
        "restart" -> Pair("shutdown -r now", true)
        "hibernate" -> Pair("pmset hibernatemode 25 && pmset hibernate now", true)
        "logoff" -> Pair("launchctl bootout user/${System.getProperty("user.name")}", true)
        "sleep" -> Pair("pmset sleepnow", false)
        "lock" -> Pair("pmset displaysleepnow", false)
        else -> throw IllegalArgumentException("Unknown Mac command: $command")
    }
    executeWithLogging(cmd, needsRoot)
}

private fun executeLinuxCommand(command: String) {
    val user = System.getProperty("user.name")
    val (cmd, needsRoot) = when (command) {
        // System commands
        "shutdown" -> Pair("shutdown -h now", true)
        "restart" -> Pair("shutdown -r now", true)
        "hibernate" -> Pair("systemctl hibernate", true)
        "logoff" -> Pair("pkill -KILL -u $user", false)
        "sleep" -> Pair("systemctl suspend", true)
        "lock" -> Pair("dm-tool lock", false) // For LightDM
        else -> throw IllegalArgumentException("Unknown Linux command: $command")
    }
    executeWithLogging(cmd, needsRoot)
}

/**
 * Helper function to execute a command and handle its output/errors.
 * @param command The command to execute.
 * @param requireRoot Whether the command requires root privileges.
 */
private fun executeWithLogging(command: String, requireRoot: Boolean = false) {
    val runtime = Runtime.getRuntime()
    val fullCommand = if (requireRoot) {
        when {
            System.getProperty("os.name").lowercase().contains("mac") ||
                    System.getProperty("os.name").lowercase().contains("nix") ||
                    System.getProperty("os.name").lowercase().contains("nux") -> "sudo $command"
            else -> command // Windows handles elevation differently
        }
    } else {
        command
    }

    try {
        val process = runtime.exec(fullCommand)
        val exitCode = process.waitFor()

        // Read command output using used to properly close resources
        val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
        val error = BufferedReader(InputStreamReader(process.errorStream)).use { it.readText() }

        when {
            exitCode == 0 -> {
                println("Command executed successfully: $fullCommand")
                if (output.isNotEmpty()) println("Output: $output")
            }
            else -> {
                println("Command failed with exit code $exitCode: $fullCommand")
                if (error.isNotEmpty()) println("Error: $error")
            }
        }
    } catch (e: Exception) {
        println("Error executing command '$fullCommand': ${e.message}")
        throw e
    }
}
