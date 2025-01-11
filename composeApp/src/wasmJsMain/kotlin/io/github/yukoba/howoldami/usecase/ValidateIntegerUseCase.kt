package io.github.yukoba.howoldami.usecase

class ValidateIntegerUseCase {
    operator fun invoke(
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
}