import io.github.kez_lab.multipatform.full.model.Priority
import io.github.kez_lab.multipatform.full.model.Task
import io.github.kez_lab.multipatform.full.module
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertTrue

class IntegrationTest {

    @Test
    fun testIntegration() {
        testApplication {
            application {
                module()
            }

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

            runBlocking {
                // 1. 새로운 Task 추가
                val newTask = Task("TestTask", "A new task for testing", Priority.High)
                val postResponse = client.post("/tasks") {
                    contentType(ContentType.Application.Json)
                    setBody(newTask)
                }
                assertEquals(HttpStatusCode.NoContent, postResponse.status)

                // 2. 모든 Task 가져오기
                val tasks: List<Task> = client.get("/tasks").body()
                assertTrue { tasks.contains(newTask) }

                // 3. 이름으로 Task 검색
                val taskByName: Task = client.get("/tasks/byName/${newTask.name}").body()
                assertEquals(newTask, taskByName)

                // 4. 우선순위로 Task 검색
                val tasksByPriority: List<Task> =
                    client.get("/tasks/byPriority/${newTask.priority}").body()
                assertTrue { tasksByPriority.contains(newTask) }

                // 5. Task 삭제
                val deleteResponse = client.delete("/tasks/${newTask.name}")
                assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

                // 6. 삭제 확인
                val remainingTasks: List<Task> = client.get("/tasks").body()
                assertTrue { !remainingTasks.contains(newTask) }
            }
        }
    }
}