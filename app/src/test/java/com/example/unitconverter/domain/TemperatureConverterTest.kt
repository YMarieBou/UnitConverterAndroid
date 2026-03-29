package com.example.unitconverter.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class TemperatureConverterTest {
    private val converter = TemperatureConverter()

    @Test
    fun `convert Celsius to Fahrenheit`() {
        val result = converter.convert(0.0, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT)
        assertEquals(32.0, result, 0.01)
    }

    @Test
    fun `convert Fahrenheit to Celsius`() {
        val result = converter.convert(32.0, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS)
        assertEquals(0.0, result, 0.01)
    }

    @Test
    fun `convert Celsius to Kelvin`() {
        val result = converter.convert(0.0, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN)
        assertEquals(273.15, result, 0.01)
    }

    @Test
    fun `convert Kelvin to Celsius`() {
        val result = converter.convert(273.15, TemperatureUnit.KELVIN, TemperatureUnit.CELSIUS)
        assertEquals(0.0, result, 0.01)
    }
}