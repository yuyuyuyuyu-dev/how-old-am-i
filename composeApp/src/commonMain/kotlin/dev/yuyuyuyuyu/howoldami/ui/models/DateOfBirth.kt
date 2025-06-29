package dev.yuyuyuyuyu.howoldami.ui.models

import androidx.annotation.IntRange

data class DateOfBirth(
    @param:IntRange(from = Int.MIN_VALUE.toLong(), to = Int.MAX_VALUE.toLong()) val year: String,
    @param:IntRange(from = 1, to = 12) val month: String,
    @param:IntRange(from = 1, to = 31) val day: String,
) {
    init {
        require(month.toInt() in 1..12)
        require(day.toInt() in 1..31)
    }
}
