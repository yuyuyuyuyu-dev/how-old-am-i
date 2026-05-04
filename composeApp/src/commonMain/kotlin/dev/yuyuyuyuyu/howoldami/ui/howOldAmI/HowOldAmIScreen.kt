package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.yuyuyuyuyu.howoldami.ui.components.DateOfBirthInputField
import howoldami.composeapp.generated.resources.Res
import howoldami.composeapp.generated.resources.please_input_your_date_of_birth
import howoldami.composeapp.generated.resources.you_are_n_years_old
import org.jetbrains.compose.resources.stringResource

@Composable
fun HowOldAmIScreen(
    viewModel: HowOldAmIViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.spacedBy(
                16.dp,
                Alignment.CenterVertically,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.please_input_your_date_of_birth))

        DateOfBirthInputField(
            value = uiState.dateOfBirth,
            onValueChange = viewModel::onDateOfBirthChanged,
        )

        Text(stringResource(Res.string.you_are_n_years_old, uiState.age))
    }
}
