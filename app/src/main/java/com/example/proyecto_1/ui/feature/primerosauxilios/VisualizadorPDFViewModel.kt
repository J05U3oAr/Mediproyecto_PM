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

//Controla si el PDF siguen cargando mediante la bandera isLoading
data class VisualizadorPDFUiState(
    val isLoading: Boolean = true     // true mientras se prepara/carga el contenido del PDF
)

//Simula una carga inicial antes de mostrar el contenido del PDF en la UI
class VisualizadorPDFViewModel : ViewModel() {

    // uiState - Flujo de solo lectura que observa la UI
    private val _uiState = MutableStateFlow(VisualizadorPDFUiState())
    val uiState: StateFlow<VisualizadorPDFUiState> = _uiState.asStateFlow()

    // Inicia la simulación de carga del PDF
    init {
        cargarDatos()
    }

    //Mantiene isLoading en true durante 2 segundos y luego lo cambia a false
    private fun cargarDatos() {
        viewModelScope.launch {
            // Activa el indicador de carga
            _uiState.value = VisualizadorPDFUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            // Desactiva el indicador de carga para mostrar el PDF
            _uiState.value = VisualizadorPDFUiState(isLoading = false)
        }
    }
}
