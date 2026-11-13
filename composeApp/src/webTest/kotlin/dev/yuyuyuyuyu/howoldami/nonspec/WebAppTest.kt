@file:OptIn(ExperimentalWasmJsInterop::class, ExperimentalTestApi::class)

package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.WebApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

@JsFun("() => document.documentElement.getAttribute('lang')")
private external fun documentLanguage(): String?

@JsFun("() => document.documentElement.removeAttribute('lang')")
private external fun removeDocumentLanguage()

@JsFun(
    """(language) => {
        Object.defineProperty(navigator, 'language', { value: language, configurable: true });
        Object.defineProperty(navigator, 'languages', { value: [language], configurable: true });
    }""",
)
private external fun setBrowserLanguage(language: String)

@JsFun("() => { delete navigator.language; delete navigator.languages; }")
private external fun restoreBrowserLanguage()

class WebAppTest {
    @Test
    fun `should mark the page as English for a browser in English`() =
        runComposeUiTest {
            inBrowserIn("en-US") {
                // Act
                setContent { WebApp() }

                // Assert
                assertEquals("en", awaitDocumentLanguage())
            }
        }

    @Test
    fun `should mark the page as Japanese for a browser in Japanese`() =
        runComposeUiTest {
            inBrowserIn("ja-JP") {
                // Act
                setContent { WebApp() }

                // Assert
                assertEquals("ja", awaitDocumentLanguage())
            }
        }

    @Test
    fun `should mark the page as English for a browser in a language the app does not have`() =
        runComposeUiTest {
            inBrowserIn("fr-FR") {
                // Act
                setContent { WebApp() }

                // Assert
                assertEquals("en", awaitDocumentLanguage())
            }
        }

    private inline fun inBrowserIn(
        language: String,
        block: () -> Unit,
    ) {
        removeDocumentLanguage()
        setBrowserLanguage(language)
        try {
            block()
        } finally {
            restoreBrowserLanguage()
        }
    }

    private suspend fun ComposeUiTest.awaitDocumentLanguage(): String? {
        val deadline = TimeSource.Monotonic.markNow() + 1.seconds
        while (documentLanguage() == null && deadline.hasNotPassedNow()) {
            awaitIdle()
            withContext(Dispatchers.Default) { delay(10.milliseconds) }
        }
        return documentLanguage()
    }
}
