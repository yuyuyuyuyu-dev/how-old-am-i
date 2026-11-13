package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class DateOfBirthInputTest {
    @Test
    fun `should ignore letters typed into the year`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("yearInput").performTextInput("abc")

            // Assert
            onNodeWithTag("yearInput").assertTextEquals("")
        }

    @Test
    fun `should ignore letters typed into the month`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("monthInput").performTextInput("abc")

            // Assert
            onNodeWithTag("monthInput").assertTextEquals("")
        }

    @Test
    fun `should ignore letters typed into the day`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("dayInput").performTextInput("abc")

            // Assert
            onNodeWithTag("dayInput").assertTextEquals("")
        }

    @Test
    fun `should keep the month when the next digit would make it greater than 12`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            onNodeWithTag("monthInput").performTextInput("1")

            // Act
            onNodeWithTag("monthInput").performTextInput("3")

            // Assert
            onNodeWithTag("monthInput").assertTextEquals("1")
        }

    @Test
    fun `should keep the day when the next digit would make it greater than 31`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            onNodeWithTag("dayInput").performTextInput("3")

            // Act
            onNodeWithTag("dayInput").performTextInput("2")

            // Assert
            onNodeWithTag("dayInput").assertTextEquals("3")
        }

    @Test
    fun `should empty the year and show no age when the year is deleted`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("yearInput").performTextClearance()

            // Assert
            onNodeWithTag("yearInput").assertTextEquals("")
            assertNoAge()
        }

    @Test
    fun `should empty the month and show no age when the month is deleted`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("monthInput").performTextClearance()

            // Assert
            onNodeWithTag("monthInput").assertTextEquals("")
            assertNoAge()
        }

    @Test
    fun `should empty the day and show no age when the day is deleted`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("dayInput").performTextClearance()

            // Assert
            onNodeWithTag("dayInput").assertTextEquals("")
            assertNoAge()
        }
}
