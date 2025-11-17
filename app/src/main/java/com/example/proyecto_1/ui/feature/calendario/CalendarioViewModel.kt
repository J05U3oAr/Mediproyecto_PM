//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//CalendarioUiState - Estado de la pantalla de calendario/notificaciones
//Utiliza isLoading para indicar si se debe mostrar la pantalla de carga inicial
data class CalendarioUiState(
    val isLoading: Boolean = true
)

//Simula una carga inicial y expone el estado de la UI mediante StateFlow
class CalendarioViewModel : ViewModel() {

    // uiState - Flujo inmutable observado desde la UI (Compose)
    private val _uiState = MutableStateFlow(CalendarioUiState())
    val uiState: StateFlow<CalendarioUiState> = _uiState.asStateFlow()

    // Inicia la simulación de carga de datos del calendario
    init {
        cargarDatos()
    }
    //Mantiene isLoading en true por 2 segundos y luego lo cambia a false
    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = CalendarioUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = CalendarioUiState(isLoading = false)
        }
    }
}
