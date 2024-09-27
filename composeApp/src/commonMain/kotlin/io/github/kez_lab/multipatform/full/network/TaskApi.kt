package io.github.kez_lab.multipatform.full.network

import io.github.kez_lab.multipatform.full.model.Task
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TaskApi(private val httpClient: HttpClient) {

    suspend fun getAllTasks(): List<Task> {
        return try {
            val response: List<Task> = httpClient.get("tasks").body()
            response.sortedByDescending { it.priority.ordinal }
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
            httpClient.post("tasks") {
                contentType(ContentType.Application.Json)
                setBody(task)
            }
        } catch (e: Exception) {
            println("Error updating task: ${e.message}")
        }
    }
}