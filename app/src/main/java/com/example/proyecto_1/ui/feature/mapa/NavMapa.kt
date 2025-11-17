//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.mapa

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

//Función de extensión para NavGraphBuilder que registra el grafo de navegación del mapa
//Configura la navegación con opción de retroceso
//Parámetros:
//nav: NavController para manejar la navegación
fun NavGraphBuilder.registrarGrafoMapa(nav: NavController) {
    //Registra la ruta composable usando el objeto serializable MapaContactos
    composable<MapaContactos> {
        PantallaMapaContactos(
            //Callback que regresa a la pantalla anterior
            onVolver = { nav.popBackStack() }
        )
    }
}