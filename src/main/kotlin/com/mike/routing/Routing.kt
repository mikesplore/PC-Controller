package com.mike.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {

        get("ping"){
            call.respondText("Server is up and running", status = HttpStatusCode.OK)
        }


        get("/") {
            val formFile = this::class.java.classLoader.getResource("form.html")?.toURI()?.let { File(it) }
            if (formFile != null && formFile.exists()) {
                call.respondFile(formFile)
            } else {
                call.respondText("Form not found", status = HttpStatusCode.NotFound)
            }
        }

        configureSystemInfoRouting()
        configureStatusManager()

    }
}
