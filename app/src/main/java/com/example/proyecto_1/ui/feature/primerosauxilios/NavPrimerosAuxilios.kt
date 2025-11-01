package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registrarGrafoPrimerosAuxilios(nav: NavController) {
    composable<PrimerosAuxilios> {
        PantallaPrimerosAuxilios(
            navController = nav,
            onVolver = { nav.popBackStack() },
            onAbrirGuia = {},
            mostrarBarraInferior = true
        )
    }
}
