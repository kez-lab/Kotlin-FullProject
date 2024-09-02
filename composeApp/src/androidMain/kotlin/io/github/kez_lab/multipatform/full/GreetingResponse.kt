package io.github.kez_lab.multipatform.full

@Serializable
data class GreetingResponse(val message: String)

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun fetchGreeting(): String {
    val response: GreetingResponse = client.get("http://10.0.2.2:8080/")
    return response.message
}