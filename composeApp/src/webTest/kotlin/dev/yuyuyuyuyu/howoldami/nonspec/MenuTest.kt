package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MenuTest {
    @Test
    fun `should keep the date of birth after coming back from the open source licenses`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))
            enterDateOfBirth(year = "2000", month = "6", day = "21")

            // Act
            onNodeWithTag("SimpleTopAppBar.MenuButton").performClick()
            onNodeWithTag("SimpleTopAppBar.OpenSourceLicensesButton").performClick()
            onNodeWithTag("SimpleTopAppBar.NavigateBackButton").performClick()

            // Assert
            onNodeWithTag("yearInput").assertTextEquals("2000")
            onNodeWithTag("monthInput").assertTextEquals("6")
            onNodeWithTag("dayInput").assertTextEquals("21")
            assertAge(26)
        }

    @Test
    fun `should open the source code repository from the menu`() =
        runComposeUiTest {
            // Arrange
            val openedUris = mutableListOf<String>()
            val uriHandler =
                object : UriHandler {
                    override fun openUri(uri: String) {
                        openedUris += uri
                    }
                }
            setContent {
                CompositionLocalProvider(LocalUriHandler provides uriHandler) {
                    App()
                }
            }

            // Act
            onNodeWithTag("SimpleTopAppBar.MenuButton").performClick()
            onNodeWithTag("SimpleTopAppBar.SourceCodeButton").performClick()

            // Assert
            assertEquals(listOf("https://github.com/yuyuyuyuyu-dev/how-old-am-i"), openedUris)
        }
}
