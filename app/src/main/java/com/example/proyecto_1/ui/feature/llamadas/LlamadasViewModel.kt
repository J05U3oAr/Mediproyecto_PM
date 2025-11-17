//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.llamadas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Controla si la pantalla debe mostrar el loader inicial mediante la bandera isLoading
data class LlamadasUiState(
    val isLoading: Boolean = true   // true mientras se simula la carga de datos
)

//Simula una carga inicial y expone el estado de la UI usando StateFlow
class LlamadasViewModel : ViewModel() {

    // uiState - Versión de solo lectura observada por la UI (Compose)
    private val _uiState = MutableStateFlow(LlamadasUiState())
    val uiState: StateFlow<LlamadasUiState> = _uiState.asStateFlow()

    // Inicia la simulación de carga de la pantalla de llamadas
    init {
        cargarDatos()
    }

    //Mantiene el estado isLoading en true durante 2 segundos y luego lo cambia a false
    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = LlamadasUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = LlamadasUiState(isLoading = false)
        }
    }
}