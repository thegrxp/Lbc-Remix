package com.ainrom.lbcremix.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.ainrom.lbcremix.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.*

class AlbumsTest {
    private lateinit var textField: SemanticsNodeInteraction
    private lateinit var loading: SemanticsNodeInteraction
    private lateinit var column: SemanticsNodeInteraction

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LaunchActivity>()

    @Before
    fun setUp() {
        textField =
            composeTestRule.onNode(hasTestTag(composeTestRule.activity.getString(R.string.instrumented_text_field)))
        loading =
            composeTestRule.onNode(hasTestTag(composeTestRule.activity.getString(R.string.instrumented_loading)))
        column =
            composeTestRule.onNode(hasTestTag(composeTestRule.activity.getString(R.string.instrumented_column)))
    }

    @Test
    fun screen_displayLoading_thenColumn() {
        textField.assertIsDisplayed()
        loading.assertIsDisplayed()

        // Loading content from ViewModel
        runBlocking {
            delay(500)
        }
        // LazyColumn is filled with albums
        column.assertIsDisplayed()
    }

    @Test
    fun screen_filterAlbums_displayColumn() {
        // Filter albums
        textField.performTextInput("12")

        // Loading filtered content from ViewModel
        runBlocking {
            delay(500)
        }

        // LazyColumn is filled with filtered albums
        column.assertIsDisplayed()
    }
}
