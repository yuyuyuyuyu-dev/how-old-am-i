package io.github.yukoba.howoldami.domain

fun validateIntegerUseCase(
    string: String,
    onSuccess: (String) -> Unit,
    onEmpty: (String) -> Unit = {},
    onFailure: (String) -> Unit = {},
) {
    if (string.isEmpty()) {
        onEmpty(string)
    } else if (string.all { it.isDigit() }) {
        onSuccess(string)
    } else {
        onFailure(string)
    }
}
