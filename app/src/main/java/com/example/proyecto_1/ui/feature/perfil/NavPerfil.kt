package com.example.proyecto_1.ui.feature.perfil

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.app.Login
import com.example.proyecto_1.ui.app.Perfil

fun NavGraphBuilder.registrarGrafoPerfil(
    nav: NavController,
    sessionManager: SessionManager
) {
    composable<Perfil> {
        PantallaPerfil(
            sessionManager = sessionManager,
            onCerrarSesion = {
                // Cerrar sesión y limpiar datos
                sessionManager.logout()

                // Navegar a Login
                nav.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navController = nav
        )
    }
}