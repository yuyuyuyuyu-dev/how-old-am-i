package dev.yuyuyuyuyu.howoldami

internal expect fun replaceBrowserDate(
    epochMillis: Double,
    minutesBehindUtc: Double,
): () -> Unit
