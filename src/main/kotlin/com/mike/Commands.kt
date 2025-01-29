package com.mike

import kotlinx.io.IOException
import org.h2.engine.DbObject.USER
import java.util.*

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
    val runtime = Runtime.getRuntime()
    val user = System.getProperty("user.name")
    when (command) {
        // System commands
        "shutdown" -> runtime.exec("shutdown -h now")
        "restart" -> runtime.exec("shutdown -r now")
        "hibernate" -> runtime.exec("systemctl hibernate")
        "logoff" -> runtime.exec("pkill -KILL -u $user")
        "sleep" -> runtime.exec("systemctl suspend")
        "lock" -> runtime.exec("gnome-screensaver-command -l")
    }
}