package com.example.plugins

import com.example.dataaccess.MessageDao
import com.example.messagesRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        messagesRoutes(MessageDao())
    }
}
