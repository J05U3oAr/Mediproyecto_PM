package com.example.proyecto_1.ui.feature.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapaUiState(
    val isLoading: Boolean = true
)

class MapaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MapaUiState())
    val uiState: StateFlow<MapaUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = MapaUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = MapaUiState(isLoading = false)
        }
    }
}