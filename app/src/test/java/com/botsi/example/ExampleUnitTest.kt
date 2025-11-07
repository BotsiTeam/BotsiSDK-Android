package com.botsi.example

import org.junit.Test
import org.junit.Assert.*
import com.botsi.view.utils.toHexColorIfPossible

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testToHexColorIfPossible_basicRgb() {
        // Test the issue: rgb(236, 119, 171) should be parsed correctly
        val input = "rgb(236, 119, 171)"
        val expected = "#FFEC77AB" // Alpha FF (255), Red EC (236), Green 77 (119), Blue AB (171)
        val result = input.toHexColorIfPossible()

        println("[DEBUG_LOG] Input: $input")
        println("[DEBUG_LOG] Expected: $expected")
        println("[DEBUG_LOG] Actual: $result")

        assertEquals(expected, result)
    }

    @Test
    fun testToHexColorIfPossible_existingFormats() {
        // Test existing formats still work
        val testCases = mapOf(
            "#FF0000" to "#FF0000", // Already hex
            "rgb(255, 0, 0, 1.0)" to "#FFFF0000", // With alpha
            "rgba(255, 0, 0, 0.5)" to "#80FF0000" // RGBA format
        )

        testCases.forEach { (input, expected) ->
            val result = input.toHexColorIfPossible()
            println("[DEBUG_LOG] Input: $input, Expected: $expected, Actual: $result")
            assertEquals("Failed for input: $input", expected, result)
        }
    }
}
