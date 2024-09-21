package io.github.kez_lab.multipatform

import io.github.kez_lab.multipatform.full.model.Priority
import io.github.kez_lab.multipatform.full.model.Task
import io.github.kez_lab.multipatform.full.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun testGetHello() = testApplication {
        application { module() }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, Ktor!", response.bodyAsText())
    }


    @Test
    fun testAddTask() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val newTask = Task("Test Task", "Description", Priority.Medium)
        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun testGetTaskByName() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val newTask = Task("Unique Task", "Description", Priority.High)
        client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }

        val response = client.get("/tasks/byName/Unique Task")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Unique Task"))
    }

    @Test
    fun testDeleteTask() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val newTask = Task("Delete Task", "Description", Priority.Low)
        client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody(newTask)
        }

        val deleteResponse = client.delete("/tasks/Delete Task")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getResponse = client.get("/tasks/byName/Delete Task")
        assertEquals(HttpStatusCode.NotFound, getResponse.status)
    }
}
