//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Controla si la pantalla muestra el loader inicial mediante la bandera isLoading
data class RegistroUiState(
    val isLoading: Boolean = true      // true mientras se simula la carga/preparación de datos
)

//Simula una carga inicial y expone el estado de la UI usando StateFlow
class RegistroViewModel : ViewModel() {

    // uiState - Versión inmutable que observa la UI (Compose)
    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    // Inicia el proceso de carga simulada
    init {
        cargarDatos()
    }

    //Mantiene isLoading en true por 2 segundos y luego la cambia a false
    private fun cargarDatos() {
        viewModelScope.launch {
            // Activa el indicador de carga
            _uiState.value = RegistroUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            // Desactiva el indicador de carga
            _uiState.value = RegistroUiState(isLoading = false)
        }
    }
}
