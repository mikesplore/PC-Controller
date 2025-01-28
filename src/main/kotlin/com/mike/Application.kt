package com.mike

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import com.mike.routing.configureRouting
import com.mike.routing.configureSystemInfoRouting

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
    EngineMain.main(args)
}

fun Application.module() {
    install(CORS) {
        anyHost() // Allow requests from any host
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)

        broadcastServerAvailability()

    }

    configureSerialization()
    configureMonitoring()
    configureRouting()
    configureSystemInfoRouting()
}