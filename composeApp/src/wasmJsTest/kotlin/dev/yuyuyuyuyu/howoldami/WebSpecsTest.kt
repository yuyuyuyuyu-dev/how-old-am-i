@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.yuyuyuyuyu.howoldami

import kotlinx.coroutines.await
import kotlinx.coroutines.test.runTest
import org.w3c.dom.Document
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals

@JsFun(
    """() => fetch('/base/kotlin/index.html')
        .then((response) => response.text())
        .then((html) => new DOMParser().parseFromString(html, 'text/html'))""",
)
private external fun loadIndexHtml(): Promise<Document>

class WebSpecsTest {
    @Test
    fun `should not offer to translate the page`() =
        runTest {
            // Act
            val page = loadIndexHtml().await<Document>()

            // Assert
            assertEquals("no", page.documentElement?.getAttribute("translate"))
            assertEquals("notranslate", page.querySelector("meta[name=google]")?.getAttribute("content"))
        }
}
