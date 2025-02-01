package io.github.yukoba.howoldami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.yukoba.howoldami.data.types.DateOfBirth
import io.github.yukoba.howoldami.domain.validateIntegerUseCase

@Composable
fun DateOfBirthInputField(
    value: DateOfBirth,
    onValueChange: (DateOfBirth) -> Unit,
) {
    val monthFocusRequester = remember { FocusRequester() }
    val dayFocusRequester = remember { FocusRequester() }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = value.year,
            onValueChange = { newValue ->
                validateIntegerUseCase(
                    string = newValue,
                    onSuccess = {
                        onValueChange(value.copy(year = it))
                        if (it.length >= 4) {
                            monthFocusRequester.requestFocus()
                        }
                    },
                    onEmpty = {
                        onValueChange(value.copy(year = it))
                    }
                )
            },
            modifier = Modifier.width(72.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Text("/")
        TextField(
            value = value.month,
            onValueChange = { newValue ->
                validateIntegerUseCase(
                    string = newValue,
                    onSuccess = {
                        onValueChange(value.copy(month = it))

                        val month = it.toInt()
                        if (it.length >= 2 && 1 <= month && month <= 12) {
                            dayFocusRequester.requestFocus()
                        }
                    },
                    onEmpty = {
                        onValueChange(value.copy(month = it))
                    }
                )
            },
            modifier = Modifier.width(72.dp).focusRequester(monthFocusRequester),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Text("/")
        TextField(
            value = value.day,
            onValueChange = { newValue ->
                validateIntegerUseCase(
                    string = newValue,
                    onSuccess = {
                        onValueChange(value.copy(day = it))
                    },
                    onEmpty = {
                        onValueChange(value.copy(day = it))
                    }
                )
            },
            modifier = Modifier.width(72.dp).focusRequester(dayFocusRequester),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}
