package com.example.stocky

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test



class StockyInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun stockSearch() {
        composeTestRule.waitUntilNodeCount(
            hasTestTag("search_button"),
            count = 1,
            timeoutMillis = 10_000
        )

        composeTestRule
            .onNodeWithTag("search_button")
            .performClick()

        composeTestRule
            .onNodeWithTag("search_input")
            .performTextInput("TSLA")

        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithText("TSLA").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("TSLA")
            .assertExists()
    }
}