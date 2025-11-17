//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.registro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.app.Inicio
import com.example.proyecto_1.ui.app.Registro

//Registra el destino Registro en el NavGraph y maneja la navegación cuando el perfil se completa
fun NavGraphBuilder.registrarGrafoRegistro(
    nav: NavController,            // Controlador de navegación de la app
    sessionManager: SessionManager // Maneja el estado de sesión y perfil del usuario
) {
    composable<Registro> {
        //PantallaRegistro - Pantalla donde el usuario completa su perfil/registro
        PantallaRegistro(
            sessionManager = sessionManager,
            onPerfilCompletado = {
                // Marca el perfil como completado en SessionManager
                sessionManager.markProfileCompleted()

                // Navega a Inicio y elimina Registro del back stack
                nav.navigate(Inicio) {
                    popUpTo<Registro> { inclusive = true } // Quita Registro del stack
                    launchSingleTop = true                 // Evita múltiples copias de Inicio
                }
            }
        )
    }
}