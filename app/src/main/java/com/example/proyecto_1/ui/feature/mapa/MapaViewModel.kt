//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//La bandera isLoading indica si se debe mostrar la pantalla de carga
data class MapaUiState(
    val isLoading: Boolean = true
)

//Simula una carga inicial antes de mostrar el mapa, contactos y lugares cercanos
class MapaViewModel : ViewModel() {

    // uiState - Flujo inmutable observado desde la UI (Compose)
    private val _uiState = MutableStateFlow(MapaUiState())
    val uiState: StateFlow<MapaUiState> = _uiState.asStateFlow()

    // Llama a cargarDatos() para iniciar la simulación de carga
    init {
        cargarDatos()
    }

    //Mantiene isLoading en true durante 2 segundos y luego lo cambia a false
    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = MapaUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = MapaUiState(isLoading = false)
        }
    }
}