//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//PrimerosAuxiliosUiState - Estado de la pantalla de primeros auxilios
//Controla si la pantalla está cargando (isLoading) para mostrar el loader o el contenido
data class PrimerosAuxiliosUiState(
    val isLoading: Boolean = true     // true mientras se simula la carga de datos
)

//PrimerosAuxiliosViewModel - Lógica de presentación para la pantalla de guías de primeros auxilios
//Simula una carga inicial de datos y expone el estado de la UI mediante StateFlow
class PrimerosAuxiliosViewModel : ViewModel() {

    // uiState - Versión de solo lectura que se observa desde la UI (Compose)
    private val _uiState = MutableStateFlow(PrimerosAuxiliosUiState())
    val uiState: StateFlow<PrimerosAuxiliosUiState> = _uiState.asStateFlow()

    // Llama a cargarDatos() para simular la carga inicial
    init {
        cargarDatos()
    }

    //Muestra un estado de carga durante 2 segundos y luego marca isLoading como false
    private fun cargarDatos() {
        viewModelScope.launch {
            // Activa el indicador de carga
            _uiState.value = PrimerosAuxiliosUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos

            // Desactiva el indicador de carga y muestra el contenido
            _uiState.value = PrimerosAuxiliosUiState(isLoading = false)
        }
    }
}
