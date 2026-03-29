package com.example.unitconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unitconverter.domain.TemperatureConverter
import com.example.unitconverter.domain.TemperatureUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class TemperatureUiState(
    val inputValue: String = "",
    val fromUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val toUnit: TemperatureUnit = TemperatureUnit.FAHRENHEIT,
    val result: String = ""
)

@HiltViewModel
class TemperatureViewModel @Inject constructor(
    private val converter: TemperatureConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onInputValueChange(newValue: String) {
        _uiState.update { it.copy(inputValue = newValue) }
        convert()
    }

    fun onFromUnitChange(unit: TemperatureUnit) {
        _uiState.update { it.copy(fromUnit = unit) }
        convert()
    }

    fun onToUnitChange(unit: TemperatureUnit) {
        _uiState.update { it.copy(toUnit = unit) }
        convert()
    }

    private fun convert() {
        val input = _uiState.value.inputValue.toDoubleOrNull()
        if (input == null) {
            _uiState.update { it.copy(result = "") }
            return
        }

        val convertedValue = converter.convert(
            input,
            _uiState.value.fromUnit,
            _uiState.value.toUnit
        )
        _uiState.update { it.copy(result = String.format("%.2f", convertedValue)) }
    }
}