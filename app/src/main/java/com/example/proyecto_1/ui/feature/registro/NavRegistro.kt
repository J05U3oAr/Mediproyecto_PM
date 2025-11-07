package com.example.proyecto_1.ui.feature.registro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.app.Inicio

fun NavGraphBuilder.registrarGrafoRegistro(
    nav: NavController,
    sessionManager: SessionManager
) {
    composable<Registro> {
        PantallaRegistro(
            sessionManager = sessionManager,
            onPerfilCompletado = {
                // Marcar perfil como completado
                sessionManager.markProfileCompleted()

                // Navegar a Inicio
                nav.navigate(Inicio) {
                    popUpTo<Registro> { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}