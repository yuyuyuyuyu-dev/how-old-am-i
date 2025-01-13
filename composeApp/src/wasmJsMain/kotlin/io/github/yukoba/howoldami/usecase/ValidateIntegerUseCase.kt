package io.github.yukoba.howoldami.usecase

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