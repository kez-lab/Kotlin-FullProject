package io.github.kez_lab.multipatform.full

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform