package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

// Destinos de navegación
@Serializable object PrimerosAuxilios
@Serializable data class VisualizadorPDF(val nombreArchivo: String)

fun NavGraphBuilder.registrarGrafoPrimerosAuxilios(nav: NavController) {
    // Pantalla principal de Primeros Auxilios
    composable<PrimerosAuxilios> {
        PantallaPrimerosAuxilios(
            navController = nav,
            onVolver = { nav.popBackStack() },
            onAbrirGuia = { nombreArchivo ->
                // Navegar a la pantalla del visualizador PDF
                nav.navigate(VisualizadorPDF(nombreArchivo = nombreArchivo))
            },
            mostrarBarraInferior = true
        )
    }

    // Pantalla del visualizador de PDF
    composable<VisualizadorPDF> { backStackEntry ->
        val args = backStackEntry.arguments
        val nombreArchivo = args?.getString("nombreArchivo") ?: ""

        PantallaVisualizadorPDF(
            nombreArchivo = nombreArchivo,
            onVolver = { nav.popBackStack() }
        )
    }
}