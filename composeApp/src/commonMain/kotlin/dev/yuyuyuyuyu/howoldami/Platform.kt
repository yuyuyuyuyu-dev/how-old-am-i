package dev.yuyuyuyuyu.howoldami

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
