package io.github.kez_lab.multipatform.full.network

import io.github.kez_lab.multipatform.full.model.Task
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TaskApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            host = "https://ktor-sample-project-env-2.eba-t3qywkvr.ap-northeast-2.elasticbeanstalk.com"
        }
    }

    suspend fun getAllTasks(): List<Task> {
        return try {
            httpClient.get("tasks").body()
        } catch (e: Exception) {
            println("Error fetching tasks: ${e.message}")
            emptyList()
        }
    }

    suspend fun removeTask(task: Task) {
        try {
            httpClient.delete("tasks/${task.name}")
        } catch (e: Exception) {
            println("Error removing task: ${e.message}")
        }
    }

    suspend fun updateTask(task: Task) {
        try {
            httpClient.post("tasks/${task.name}") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }
        } catch (e: Exception) {
            println("Error updating task: ${e.message}")
        }
    }
}