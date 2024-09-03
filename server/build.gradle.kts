plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktorServer)
    application
}

group = "io.github.kez_lab.multipatform.full"
version = "1.0.0"
application {
    mainClass.set("io.github.kez_lab.multipatform.full.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.kotlin.test.junit)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.ktor.server.content.negotiation.jvm)
}