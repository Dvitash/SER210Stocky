package com.example.stocky

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test



class StockyInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun stockSearch() {

        // Navigate to search screen
        composeTestRule
            .onNodeWithText("Add Stocks")
            .performClick()
            Thread.sleep(2000)

        // Search for stock TSLA
        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("TSLA")
            Thread.sleep(2000)

        // Verify stock is searched correctly
        composeTestRule
            .onNodeWithText("TSLA")
            .assertExists()
            Thread.sleep(2000)


    }
}
