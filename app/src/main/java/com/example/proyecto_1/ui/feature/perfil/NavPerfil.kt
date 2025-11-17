//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.perfil

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.app.Login
import com.example.proyecto_1.ui.app.Perfil

//Función de extensión para NavGraphBuilder que registra el grafo de navegación del perfil
//Maneja la navegación del perfil y el proceso de cierre de sesión
//Parámetros:
//nav: NavController para manejar la navegación
//sessionManager: Gestor de sesión para cerrar sesión y limpiar datos
fun NavGraphBuilder.registrarGrafoPerfil(
    nav: NavController,
    sessionManager: SessionManager
) {
    // Registra la ruta composable usando el objeto serializable Perfil
    composable<Perfil> {
        PantallaPerfil(
            sessionManager = sessionManager,
            onCerrarSesion = {
                // Cierra la sesión y limpia todos los datos del usuario
                sessionManager.logout()

                // Navega a la pantalla de Login
                nav.navigate(Login) {
                    // Limpia todo el stack de navegación incluyendo Login
                    popUpTo(0) { inclusive = true }
                    // Evita múltiples instancias de Login
                    launchSingleTop = true
                }
            },
            navController = nav
        )
    }
}