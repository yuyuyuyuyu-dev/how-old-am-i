package dev.yuyuyuyuyu.howoldami.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BaseNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) = TextField(
    value = value,
    onValueChange = {
        if (it.toIntOrNull() == null) {
            return@TextField
        }

        onValueChange(it)
    },
    modifier = modifier.width(72.dp),
    textStyle = textStyle.copy(textAlign = TextAlign.Center),
    keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
)
