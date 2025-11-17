//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.llamadas

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.ui.app.Inicio

// Función de extensión para NavGraphBuilder que registra el grafo de navegación de llamadas
// Configura la navegación de regreso al inicio después de la llamada
fun NavGraphBuilder.registrarGrafoLlamadas(nav: NavController) {
    // Registra la ruta composable usando el objeto serializable Llamadas
    composable<Llamadas> {
        ConfirmarLlamadaScreen(
            // Callback que navega al inicio limpiando el stack hasta Inicio
            onVolverInicio = {
                nav.navigate(Inicio) {
                    popUpTo(Inicio) { inclusive = false } // No elimina Inicio del stack
                    launchSingleTop = true // Evita múltiples instancias de Inicio
                }
            }
        )
    }
}