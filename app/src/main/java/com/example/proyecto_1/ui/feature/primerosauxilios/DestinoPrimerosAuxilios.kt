package com.example.proyecto_1.ui.feature.primerosauxilios

import kotlinx.serialization.Serializable

// Destino para la pantalla principal de Primeros Auxilios
@Serializable
object PrimerosAuxilios

// Destino para el visualizador de PDF con parámetro de nombre de archivo
@Serializable
data class VisualizadorPDF(val nombreArchivo: String)