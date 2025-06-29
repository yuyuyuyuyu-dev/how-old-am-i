package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.yuyuyuyuyu.howoldami.ui.components.DateOfBirthInputField
import dev.yuyuyuyuyu.howoldami.ui.models.DateOfBirth
import how_old_am_i.composeapp.generated.resources.Res
import how_old_am_i.composeapp.generated.resources.please_input_your_date_of_birth
import how_old_am_i.composeapp.generated.resources.you_are_n_years_old
import org.jetbrains.compose.resources.stringResource

@Composable
fun HowOldAmI(state: HowOldAmIScreen.State, modifier: Modifier = Modifier) {
    var dateOfBirth by rememberSaveable { mutableStateOf(DateOfBirth(year = "", month = "", day = "")) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.please_input_your_date_of_birth))

        DateOfBirthInputField(
            value = dateOfBirth,
            onValueChange = {
                dateOfBirth = it
                state.eventSink(HowOldAmIScreen.Event.DateOfBirthChanged(newDateOfBirth = it))
            },
        )

        Text(stringResource(Res.string.you_are_n_years_old, state.age))
    }
}
