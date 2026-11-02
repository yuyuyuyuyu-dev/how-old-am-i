package dev.yuyuyuyuyu.howoldami

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIViewModelImpl
import kotlinx.datetime.LocalDate
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class SpecsTest {
    @Test
    fun `should show the age calculated from the entered date of birth and today`() =
        runComposeUiTest {
            // Arrange
            val jst = UtcOffset(hours = 9).asTimeZone()
            val today =
                object : Clock {
                    override fun now() = LocalDate(2023, 4, 6).atStartOfDayIn(jst)
                }
            val viewModel = HowOldAmIViewModelImpl(CalculateAgeUseCase(today, jst))
            setContent {
                HowOldAmIScreen(viewModel)
            }

            // Act
            onNodeWithTag("yearInput").performTextInput("2016")
            onNodeWithTag("monthInput").performTextInput("10")
            onNodeWithTag("dayInput").performTextInput("15")

            // Assert
            onNodeWithTag("ageText").assertContentDescriptionEquals("6")
        }
}
