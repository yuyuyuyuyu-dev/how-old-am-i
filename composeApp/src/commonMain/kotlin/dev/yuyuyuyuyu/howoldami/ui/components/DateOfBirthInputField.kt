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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.yuyuyuyuyu.howoldami.ui.model.DateOfBirth

@Composable
fun DateOfBirthInputField(
    value: DateOfBirth,
    onValueChange: (DateOfBirth) -> Unit,
    modifier: Modifier = Modifier
) {
    val monthFocusRequester = remember { FocusRequester() }
    val dayFocusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseNumberTextField(
            value = value.year,
            onValueChange = {
                onValueChange(value.copy(year = it))
                if (it.length >= 4) {
                    monthFocusRequester.requestFocus()
                }
            },
            modifier = Modifier.testTag("yearInput"),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Text("/")
        DateSegmentField(
            value = value.month,
            validRange = 0..12,
            next = dayFocusRequester,
            modifier = Modifier.focusRequester(monthFocusRequester).testTag("monthInput"),
            onValueChange = { onValueChange(value.copy(month = it)) }
        )
        Text("/")
        DateSegmentField(
            value = value.day,
            validRange = 0..31,
            next = null,
            modifier = Modifier.focusRequester(dayFocusRequester).testTag("dayInput"),
            onValueChange = { onValueChange(value.copy(day = it)) }
        )
    }
}

@Composable
private fun DateSegmentField(
    value: String,
    validRange: IntRange,
    next: FocusRequester?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    BaseNumberTextField(
        value = value,
        onValueChange = {
            when {
                it == "" -> onValueChange(it)

                it.toInt() !in validRange -> Unit

                else -> {
                    onValueChange(it)
                    if (it.length >= 2) {
                        next?.requestFocus() ?: focusManager.clearFocus()
                    }
                }
            }
        },
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = if (next != null) ImeAction.Next else ImeAction.Done
        )
    )
}
