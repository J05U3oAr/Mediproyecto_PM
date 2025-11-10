package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

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
            mostrarBarraInferior = false
        )
    }

    // Pantalla del visualizador de PDF
    composable<VisualizadorPDF> { backStackEntry ->
        val args: VisualizadorPDF = backStackEntry.toRoute()

        PantallaVisualizadorPDF(
            nombreArchivo = args.nombreArchivo,
            onVolver = { nav.popBackStack() }
        )
    }
}