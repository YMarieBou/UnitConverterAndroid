package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unitconverter.domain.TemperatureUnit
import com.example.unitconverter.ui.TemperatureViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TemperatureConverterScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureConverterScreen(viewModel: TemperatureViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val resultColor = remember(uiState.result, uiState.toUnit) {
        val value = uiState.result.toDoubleOrNull()
        if (value == null) {
            Color.Black
        } else {
            // Convert to Celsius for a consistent color logic
            val celsiusValue = when (uiState.toUnit) {
                TemperatureUnit.CELSIUS -> value
                TemperatureUnit.FAHRENHEIT -> (value - 32) * 5 / 9
                TemperatureUnit.KELVIN -> value - 273.15
            }
            when {
                celsiusValue <= 10 -> Color.Blue // Cold
                celsiusValue <= 25 -> Color(0xFFFFA500) // Warm (Orange)
                else -> Color.Red // Hot
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Temperature Converter",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp
                        )
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8F00FF),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = uiState.inputValue,
                onValueChange = { viewModel.onInputValueChange(it) },
                label = { Text("Enter temperature") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inputField")
            )

            Row(modifier = Modifier.padding(vertical = 16.dp)) {
                UnitDropdown(
                    label = "From",
                    selectedUnit = uiState.fromUnit,
                    onUnitSelected = { viewModel.onFromUnitChange(it) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("fromDropdown")
                )
                UnitDropdown(
                    label = "To",
                    selectedUnit = uiState.toUnit,
                    onUnitSelected = { viewModel.onToUnitChange(it) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("toDropdown")
                )
            }

            Text(
                text = "Result: ${uiState.result}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = resultColor,
                modifier = Modifier.testTag("resultText")
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(
    label: String,
    selectedUnit: TemperatureUnit,
    onUnitSelected: (TemperatureUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedUnit.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TemperatureUnit.values().forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.name) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}