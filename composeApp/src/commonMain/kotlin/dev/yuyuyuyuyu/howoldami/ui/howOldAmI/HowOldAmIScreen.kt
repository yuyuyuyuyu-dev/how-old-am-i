package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.yuyuyuyuyu.howoldami.ui.models.DateOfBirth

data object HowOldAmIScreen : Screen {
    data class State(
        val age: String,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class DateOfBirthChanged(val newDateOfBirth: DateOfBirth) : Event()
    }
}
