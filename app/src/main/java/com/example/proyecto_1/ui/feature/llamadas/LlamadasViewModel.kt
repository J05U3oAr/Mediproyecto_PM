package com.example.proyecto_1.ui.feature.llamadas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LlamadasUiState(
    val isLoading: Boolean = true
)

class LlamadasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LlamadasUiState())
    val uiState: StateFlow<LlamadasUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = LlamadasUiState(isLoading = true)
            delay(2000) // Delay de 2 segundos
            _uiState.value = LlamadasUiState(isLoading = false)
        }
    }
}