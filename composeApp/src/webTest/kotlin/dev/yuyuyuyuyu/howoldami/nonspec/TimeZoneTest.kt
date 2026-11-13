@file:OptIn(ExperimentalWasmJsInterop::class, ExperimentalTestApi::class)

package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlin.test.Test

@JsFun("() => globalThis.Date")
private external fun browserDate(): JsAny

@JsFun("(date) => { globalThis.Date = date; }")
private external fun setBrowserDate(date: JsAny)

@JsFun(
    """(epochMillis, minutesBehindUtc) => class extends Date {
        constructor(...args) {
            if (args.length === 0) {
                super(epochMillis);
            } else {
                super(...args);
            }
        }
        static now() {
            return epochMillis;
        }
        getTimezoneOffset() {
            return minutesBehindUtc;
        }
    }""",
)
private external fun fixedBrowserDate(
    epochMillis: Double,
    minutesBehindUtc: Double,
): JsAny

class TimeZoneTest {
    @Test
    fun `should decide today by the date in the time zone of the user`() =
        runComposeUiTest {
            // Arrange
            showApp(
                now = LocalDate(2026, 6, 20).atTime(15, 0).toInstant(TimeZone.UTC),
                timeZone = UtcOffset(hours = 9).asTimeZone(),
            )

            // Act
            enterDateOfBirth(year = "2000", month = "6", day = "21")

            // Assert
            assertAge(26)
        }

    @Test
    fun `should use the date of a browser ahead of UTC`() =
        runComposeUiTest {
            inBrowserAtNoonUtc(minutesBehindUtc = -840.0) {
                // Arrange
                setContent { App() }

                // Act
                enterDateOfBirth(year = "2000", month = "6", day = "21")

                // Assert
                assertAge(26)
            }
        }

    @Test
    fun `should use the date of a browser behind UTC`() =
        runComposeUiTest {
            inBrowserAtNoonUtc(minutesBehindUtc = 720.0) {
                // Arrange
                setContent { App() }

                // Act
                enterDateOfBirth(year = "2000", month = "6", day = "21")

                // Assert
                assertAge(25)
            }
        }

    private inline fun inBrowserAtNoonUtc(
        minutesBehindUtc: Double,
        block: () -> Unit,
    ) {
        val noonUtc = LocalDate(2026, 6, 20).atTime(12, 0).toInstant(TimeZone.UTC)
        val originalDate = browserDate()
        setBrowserDate(fixedBrowserDate(noonUtc.toEpochMilliseconds().toDouble(), minutesBehindUtc))
        try {
            block()
        } finally {
            setBrowserDate(originalDate)
        }
    }
}
