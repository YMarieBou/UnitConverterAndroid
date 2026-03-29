package com.example.unitconverter.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.unitconverter.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TemperatureScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testConversionFlow() {
        // Initial state
        composeTestRule.onNodeWithTag("inputField").assertIsDisplayed()
        
        // Enter value
        composeTestRule.onNodeWithTag("inputField").performTextInput("100")
        
        // Result should update (100 C to F is 212)
        // Note: The UI by default starts with Celsius to Fahrenheit
        composeTestRule.onNodeWithText("Result: 212.00").assertIsDisplayed()
    }
}