package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PrimerosAuxiliosUiState(
    val isLoading: Boolean = true
)

class PrimerosAuxiliosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PrimerosAuxiliosUiState())
    val uiState: StateFlow<PrimerosAuxiliosUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = PrimerosAuxiliosUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = PrimerosAuxiliosUiState(isLoading = false)
        }
    }
}