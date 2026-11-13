@file:OptIn(ExperimentalTestApi::class, ExperimentalTime::class)

package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import dev.yuyuyuyuyu.howoldami.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.TimeSource

internal suspend fun ComposeUiTest.awaitNodeWithText(text: String): SemanticsNodeInteraction {
    val deadline = TimeSource.Monotonic.markNow() + 1.seconds
    while (onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isEmpty() &&
        deadline.hasNotPassedNow()
    ) {
        withContext(Dispatchers.Default) { delay(10.milliseconds) }
    }
    return onNodeWithText(text, useUnmergedTree = true)
}

internal suspend fun ComposeUiTest.assertAge(age: Int) {
    awaitNodeWithText("You are $age years old.").assertIsDisplayed()
}

internal suspend fun ComposeUiTest.assertNoAge() {
    awaitNodeWithText("You are  years old.").assertIsDisplayed()
}

internal fun ComposeUiTest.showApp(today: LocalDate) {
    showApp(now = today.atStartOfDayIn(TimeZone.UTC), timeZone = TimeZone.UTC)
}

internal fun ComposeUiTest.showApp(
    now: Instant,
    timeZone: TimeZone,
) {
    val fixedClock =
        object : Clock {
            override fun now() = now
        }

    setContent {
        App(clock = fixedClock, timeZone = timeZone)
    }
}

internal fun ComposeUiTest.enterDateOfBirth(
    year: String,
    month: String,
    day: String,
) {
    onNodeWithTag("yearInput").performTextInput(year)
    onNodeWithTag("monthInput").performTextInput(month)
    onNodeWithTag("dayInput").performTextInput(day)
}
