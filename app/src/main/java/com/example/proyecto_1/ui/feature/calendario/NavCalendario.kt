//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.calendario

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

//Función de extensión para NavGraphBuilder que registra el grafo de navegación del calendario
//Parámetros:
//nav: NavController para manejar la navegación
fun NavGraphBuilder.registrarGrafoCalendario(nav: NavController) {
    // Registra la ruta composable usando el objeto serializable Calendario
    composable<Calendario> {
        // Llama a la pantalla de calendario pasando la función de navegación hacia atrás
        PantallaCalendar(
            onVolver = { nav.popBackStack() } // Regresa a la pantalla anterior
        )
    }
}