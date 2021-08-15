package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import org.jetbrains.exposed.sql.Database

fun main() {
    val databaseHostName = System.getenv("DATABASE_HOSTNAME") ?: "localhost"
    val databasePort = System.getenv("DATABASE_PORT") ?: "23306"

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureRouting()
        configureHTTP()
        configureTemplating()

        Database.connect(
            url = "jdbc:mysql://$databaseHostName:$databasePort/ktor-starter",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
        )
    }.start(wait = true)
}
