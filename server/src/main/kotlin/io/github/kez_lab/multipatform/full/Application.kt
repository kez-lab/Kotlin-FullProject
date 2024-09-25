package io.github.kez_lab.multipatform.full

import io.github.kez_lab.multipatform.full.config.configureCors
import io.github.kez_lab.multipatform.full.config.configureJson
import io.github.kez_lab.multipatform.full.config.configureRoute
import io.ktor.server.application.Application

val repository = InMemoryTaskRepository()

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureCors()
    configureJson()
    configureRoute(repository)
}