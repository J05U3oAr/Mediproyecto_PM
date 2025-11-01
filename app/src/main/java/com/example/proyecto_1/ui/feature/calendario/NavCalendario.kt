package com.example.proyecto_1.ui.feature.calendario

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registrarGrafoCalendario(nav: NavController) {
    composable<Calendario> {
        PantallaCalendar(
            onVolver = { nav.popBackStack() }
        )
    }
}