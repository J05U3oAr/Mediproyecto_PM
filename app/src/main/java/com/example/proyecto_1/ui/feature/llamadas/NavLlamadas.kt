package com.example.proyecto_1.ui.feature.llamadas

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.ui.app.Inicio

fun NavGraphBuilder.registrarGrafoLlamadas(nav: NavController) {
    composable<Llamadas> {
        ConfirmarLlamadaScreen(
            onVolverInicio = {
                nav.navigate(Inicio) {
                    popUpTo(Inicio) { inclusive = false }
                    launchSingleTop = true
                }
            }
        )
    }
}