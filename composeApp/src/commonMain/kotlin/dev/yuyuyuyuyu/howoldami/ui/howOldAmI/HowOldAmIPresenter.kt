package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.presenter.Presenter
import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase

class HowOldAmIPresenter(
    private val calculateAgeUseCase: CalculateAgeUseCase,
) : Presenter<HowOldAmIScreen.State> {
    @Composable
    override fun present(): HowOldAmIScreen.State {
        var age by rememberSaveable { mutableStateOf("") }

        return HowOldAmIScreen.State(age = age) { event ->
            when (event) {
                is HowOldAmIScreen.Event.DateOfBirthChanged -> {
                    val year = event.newDateOfBirth.year.toIntOrNull() ?: return@State
                    val month = event.newDateOfBirth.month.toIntOrNull() ?: return@State
                    val day = event.newDateOfBirth.day.toIntOrNull() ?: return@State

                    age = calculateAgeUseCase(year, month, day).toString()
                }
            }
        }
    }
}
