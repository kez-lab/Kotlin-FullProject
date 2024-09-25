package io.github.kez_lab.multipatform.full.config

import io.github.kez_lab.multipatform.full.InMemoryTaskRepository
import io.github.kez_lab.multipatform.full.model.Priority
import io.github.kez_lab.multipatform.full.model.Task
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RootRouting
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

internal fun Application.configureRoute(repository: InMemoryTaskRepository) {
    routing {
        route("/") {
            get {
                call.respond("Hello, Ktor!")
            }
        }
        configureTaskRoute(repository)
    }
}

private fun RootRouting.configureTaskRoute(repository: InMemoryTaskRepository) {
    route("/tasks") {
        get {
            val tasks = repository.allTasks()
            call.respond(tasks)
        }
        get("/byName/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val task = repository.taskByName(name)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(task)
        }
        get("/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = repository.tasksByPriority(priority)


                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(tasks)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post {
            try {
                val task = call.receive<Task>()
                repository.addOrUpdateTask(task)
                call.respond(HttpStatusCode.NoContent)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        delete("/{taskName}") {
            val name = call.parameters["taskName"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (repository.removeTask(name)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}