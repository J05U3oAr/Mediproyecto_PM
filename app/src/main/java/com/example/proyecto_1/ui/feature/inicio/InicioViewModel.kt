//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Estado de la UI para la pantalla de Inicio
data class InicioUiState(
    val isLoading: Boolean = true
)

//ViewModel para la pantalla de Inicio
//Maneja el estado de carga de la pantalla.
//Simula una carga de 2 segundos antes de mostrar el contenido.
class InicioViewModel : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(InicioUiState())

    // Estado público inmutable para la UI
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    //Simula la carga de datos con un delay de 2 segundos
    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = InicioUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = InicioUiState(isLoading = false)
        }
    }
}