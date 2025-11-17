//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.primerosauxilios

import kotlinx.serialization.Serializable

//Objeto serializable que representa el destino de navegación para la pantalla principal
//de primeros auxilios (lista de guías)
@Serializable
object PrimerosAuxilios

//Data class serializable que representa el destino de navegación para el visualizador PDF
//Incluye el nombre del archivo PDF como parámetro de navegación
//Parámetros:
//nombreArchivo: Nombre del archivo PDF a mostrar (sin extensión en algunos casos)
@Serializable
data class VisualizadorPDF(val nombreArchivo: String)