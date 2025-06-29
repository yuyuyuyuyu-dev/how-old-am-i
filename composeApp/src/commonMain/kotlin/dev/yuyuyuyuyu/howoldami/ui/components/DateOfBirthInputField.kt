package dev.yuyuyuyuyu.howoldami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.yuyuyuyuyu.howoldami.ui.models.DateOfBirth

@Composable
fun DateOfBirthInputField(
    value: DateOfBirth,
    onValueChange: (DateOfBirth) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    val monthFocusRequester = remember { FocusRequester() }
    val dayFocusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BaseNumberTextField(
            value = value.year,
            onValueChange = {
                onValueChange(value.copy(year = it))
                if (it.length >= 4) {
                    monthFocusRequester.requestFocus()
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        Text("/")
        BaseNumberTextField(
            value = value.month,
            onValueChange = {
                if (it == "") {
                    onValueChange(value.copy(month = it))
                    return@BaseNumberTextField
                }

                if (it.toInt() !in 1..12) {
                    return@BaseNumberTextField
                }

                onValueChange(value.copy(month = it))

                if (it.length >= 2) {
                    dayFocusRequester.requestFocus()
                }
            },
            modifier = Modifier.focusRequester(monthFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        Text("/")
        BaseNumberTextField(
            value = value.day,
            onValueChange = {
                if (it == "") {
                    onValueChange(value.copy(day = it))
                    return@BaseNumberTextField
                }

                if (it.toInt() !in 1..31) {
                    return@BaseNumberTextField
                }

                onValueChange(value.copy(day = it))

                if (it.length >= 2) {
                    focusManager.clearFocus()
                }
            },
            modifier = Modifier.focusRequester(dayFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        )
    }
}
