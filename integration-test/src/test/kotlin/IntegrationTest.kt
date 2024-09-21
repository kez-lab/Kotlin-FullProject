import io.github.kez_lab.multipatform.full.model.Priority
import io.github.kez_lab.multipatform.full.model.Task
import io.github.kez_lab.multipatform.full.module
import io.github.kez_lab.multipatform.full.network.TaskApi
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertTrue

class IntegrationTest {

    /**
     * 새로운 Task를 추가하고, 정상적으로 추가되었는지 확인하는 테스트.
     */
    @Test
    fun testAddNewTask() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        isLenient = true
                        coerceInputValues = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        val taskApi = TaskApi(client)
        application { module() }

        val newTask = Task("TestTask", "A new task for testing", Priority.High)
        taskApi.updateTask(newTask)
        val tasks = taskApi.getAllTasks()
        assertTrue { tasks.contains(newTask) }
    }

    /**
     * Task를 삭제하고, 정상적으로 삭제되었는지 확인하는 테스트.
     */
    @Test
    fun testDeleteTask() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        isLenient = true
                        coerceInputValues = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        val taskApi = TaskApi(client)
        application { module() }

        val newTask = Task("DeleteTask", "Task to be deleted", Priority.Medium)
        taskApi.updateTask(newTask)
        taskApi.removeTask(newTask)
        val remainingTasks = taskApi.getAllTasks()
        assertTrue { !remainingTasks.contains(newTask) }
    }
}
