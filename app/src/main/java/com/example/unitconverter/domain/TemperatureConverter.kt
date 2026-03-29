package com.example.unitconverter.domain

enum class TemperatureUnit {
    CELSIUS, FAHRENHEIT, KELVIN
}

class TemperatureConverter {
    fun convert(value: Double, from: TemperatureUnit, to: TemperatureUnit): Double {
        val celsius = when (from) {
            TemperatureUnit.CELSIUS -> value
            TemperatureUnit.FAHRENHEIT -> (value - 32) * 5 / 9
            TemperatureUnit.KELVIN -> value - 273.15
        }

        return when (to) {
            TemperatureUnit.CELSIUS -> celsius
            TemperatureUnit.FAHRENHEIT -> celsius * 9 / 5 + 32
            TemperatureUnit.KELVIN -> celsius + 273.15
        }
    }
}