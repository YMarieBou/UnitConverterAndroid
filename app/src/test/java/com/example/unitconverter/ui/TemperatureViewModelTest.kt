package com.example.unitconverter.ui

import com.example.unitconverter.domain.TemperatureConverter
import com.example.unitconverter.domain.TemperatureUnit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TemperatureViewModelTest {

    private lateinit var viewModel: TemperatureViewModel
    private val converter = mockk<TemperatureConverter>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TemperatureViewModel(converter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when input changes, result is updated correctly`() {
        // Given
        val input = "25.0"
        every { converter.convert(25.0, any(), any()) } returns 77.0

        // When
        viewModel.onInputValueChange(input)

        // Then
        assertEquals("77.00", viewModel.uiState.value.result)
        verify { converter.convert(25.0, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT) }
    }

    @Test
    fun `when units change, conversion is re-triggered`() {
        // Given
        every { converter.convert(100.0, any(), any()) } returns 212.0 andThen 373.15
        
        // Initial input triggers first conversion
        viewModel.onInputValueChange("100")

        // When
        viewModel.onToUnitChange(TemperatureUnit.KELVIN)

        // Then
        assertEquals("373.15", viewModel.uiState.value.result)
        verify(exactly = 2) { converter.convert(100.0, any(), any()) }
    }
}