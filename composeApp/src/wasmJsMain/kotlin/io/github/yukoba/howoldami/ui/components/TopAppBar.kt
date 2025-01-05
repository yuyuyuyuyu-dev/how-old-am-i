package io.github.yukoba.howoldami.ui.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun TopAppBar(title: String) = TopAppBar(
    title = { Text(title) },
)