package com.example.proyecto_1.ui.feature.perfil

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.ui.app.Inicio
import com.example.proyecto_1.ui.app.Login
import com.example.proyecto_1.ui.app.Perfil

fun NavGraphBuilder.registrarGrafoPerfil(nav: NavController) {
    composable<Perfil> {
        PantallaPerfil(
            onCerrarSesion = {
                nav.navigate(Login) {
                    popUpTo(Inicio) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navController = nav
        )
    }
}