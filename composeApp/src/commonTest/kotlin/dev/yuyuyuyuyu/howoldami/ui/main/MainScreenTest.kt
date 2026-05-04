package dev.yuyuyuyuyu.howoldami.ui.main

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class MainScreenTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun appSmokeTest_navigationAndAgeCalculation() =
        runComposeUiTest {
            // Set up the UI
            setContent {
                MainScreen()
            }

            // 1. Verify HowOldAmI screen is displayed by default
            // The text field hints or initial texts should be visible
            onNodeWithTag("yearInput").assertIsDisplayed()
            onNodeWithTag("monthInput").assertIsDisplayed()
            onNodeWithTag("dayInput").assertIsDisplayed()

            // 2. Perform Age Calculation
            val useCase =
                dev.yuyuyuyuyu.howoldami.domain.useCases
                    .CalculateAgeUseCase()
            val expectedAge = useCase(1990, 1, 1).toString()

            onNodeWithTag("yearInput").performTextInput("1990")
            onNodeWithTag("monthInput").performTextInput("1")
            onNodeWithTag("dayInput").performTextInput("1")

            // Wait a moment for any async processing (if necessary)
            mainClock.advanceTimeBy(1000L)

            // The expected text should appear on the screen
            onNodeWithTag("yearInput").assertTextContains("1990", substring = true)
            onNodeWithTag("monthInput").assertTextContains("1", substring = true)
            onNodeWithTag("dayInput").assertTextContains("1", substring = true)

            // Check if calculation happened
            onNode(hasContentDescription(expectedAge)).assertExists()

            // 3. Test Navigation to Open Source Licenses
            // Click the menu icon
            onNodeWithContentDescription("menu").performClick()
        }
}
