package dev.yuyuyuyuyu.howoldami.spec

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class SpecsTest {
    @Test
    fun `should show the age calculated from the entered date of birth and today`() =
        runComposeUiTest {
            // Arrange
            val jst = UtcOffset(hours = 9).asTimeZone()
            val today =
                object : Clock {
                    override fun now() = LocalDate(2023, 4, 6).atStartOfDayIn(jst)
                }
            setContent { App(clock = today, timeZone = jst) }

            // Act
            onNodeWithTag("yearInput").performTextInput("2016")
            onNodeWithTag("monthInput").performTextInput("10")
            onNodeWithTag("dayInput").performTextInput("15")

            // Assert
            awaitNodeWithText("You are 6 years old.").assertIsDisplayed()
        }

    private suspend fun ComposeUiTest.awaitNodeWithText(text: String): SemanticsNodeInteraction {
        val deadline = TimeSource.Monotonic.markNow() + 1.seconds
        while (onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isEmpty() &&
            deadline.hasNotPassedNow()
        ) {
            withContext(Dispatchers.Default) { delay(10.milliseconds) }
        }
        return onNodeWithText(text, useUnmergedTree = true)
    }
}
