package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FocusTest {
    @Test
    fun `should move the focus to the month after four digits of the year`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("yearInput").performTextInput("1990")

            // Assert
            onNodeWithTag("monthInput").assertIsFocused()
        }

    @Test
    fun `should move the focus to the day after two digits of the month`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("monthInput").performTextInput("12")

            // Assert
            onNodeWithTag("dayInput").assertIsFocused()
        }

    @Test
    fun `should clear the focus after two digits of the day`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("dayInput").performTextInput("31")

            // Assert
            onNodeWithTag("dayInput").assertIsNotFocused()
        }

    @Test
    fun `should clear the focus when the outside of the inputs is clicked`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            onNodeWithTag("yearInput").performClick()

            // Act
            onRoot().performTouchInput { click(bottomCenter - Offset(0f, 1f)) }

            // Assert
            onAllNodes(isFocused()).assertCountEquals(0)
        }
}
