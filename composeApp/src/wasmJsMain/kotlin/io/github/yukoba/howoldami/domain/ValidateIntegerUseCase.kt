package io.github.yukoba.howoldami.domain

fun validateIntegerUseCase(
    string: String,
    onSuccess: (String) -> Unit,
    onFailure: () -> Unit = {},
) {
    if (string.all { it.isDigit() }) {
        onSuccess(string)
    } else {
        onFailure()
    }
}
