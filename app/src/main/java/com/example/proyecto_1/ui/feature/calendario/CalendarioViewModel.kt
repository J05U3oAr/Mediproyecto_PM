package com.example.proyecto_1.ui.feature.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CalendarioUiState(
    val isLoading: Boolean = true
)

class CalendarioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarioUiState())
    val uiState: StateFlow<CalendarioUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = CalendarioUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = CalendarioUiState(isLoading = false)
        }
    }
}