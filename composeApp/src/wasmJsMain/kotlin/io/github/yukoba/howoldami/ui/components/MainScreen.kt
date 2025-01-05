package io.github.yukoba.howoldami.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    year: String,
    month: String,
    day: String,
    age: String,
    onYearChanged: (String) -> Unit,
    onMonthChanged: (String) -> Unit,
    onDayChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("生年月日を入力してください")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = year,
                    onValueChange = onYearChanged,
                    modifier = Modifier.width(72.dp),
                )
                Text("/")
                TextField(
                    value = month,
                    onValueChange = onMonthChanged,
                    modifier = Modifier.width(72.dp),
                )
                Text("/")
                TextField(
                    value = day,
                    onValueChange = onDayChanged,
                    modifier = Modifier.width(72.dp),
                )
            }
            Text("あなたは ${age}歳 です")
        }
    }
}