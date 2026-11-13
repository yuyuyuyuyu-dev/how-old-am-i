@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.yuyuyuyuyu.howoldami.nonspec

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

internal fun replaceBrowserDate(
    epochMillis: Double,
    minutesBehindUtc: Double,
): () -> Unit {
    val originalDate = browserDate()
    setBrowserDate(fixedBrowserDate(epochMillis, minutesBehindUtc))
    return { setBrowserDate(originalDate) }
}
