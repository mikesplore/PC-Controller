package com.mike.routing

import com.mike.executeCommand
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStatusManager() {
    routing {
        post("shutdown") {
            executeCommand("shutdown")
            call.respondText("Shutting down...", status = HttpStatusCode.OK)
        }

        post("restart") {
            executeCommand("restart")
            call.respondText("Restarting...", status = HttpStatusCode.OK)
        }

        post("sleep") {
            executeCommand("sleep")
            call.respondText("Sleeping...", status = HttpStatusCode.OK)
        }

        post("hibernate") {
            executeCommand("hibernate")
            call.respondText("Hibernating...", status = HttpStatusCode.OK)
        }

        post("logoff") {
            executeCommand("logoff")
            call.respondText("Logging off...", status = HttpStatusCode.OK)
        }

        post("lock") {
            executeCommand("lock")
            call.respondText("Locking...", status = HttpStatusCode.OK)
        }

    }
}