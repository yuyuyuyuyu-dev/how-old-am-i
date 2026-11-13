package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LaunchTest {
    @Test
    fun `should show the title, the prompt, the inputs, and no age when the app starts`() =
        runComposeUiTest {
            // Act
            setContent { App() }

            // Assert
            awaitNodeWithText("How old am I").assertIsDisplayed()
            awaitNodeWithText("Please input your date of birth.").assertIsDisplayed()
            onNodeWithTag("yearInput").assertIsDisplayed()
            onNodeWithTag("monthInput").assertIsDisplayed()
            onNodeWithTag("dayInput").assertIsDisplayed()
            assertNoAge()
        }
}
