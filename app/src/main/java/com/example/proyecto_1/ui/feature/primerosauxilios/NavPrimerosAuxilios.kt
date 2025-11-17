//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

//Función de extensión para NavGraphBuilder que registra el grafo de navegación
//de primeros auxilios con dos rutas: lista de guías y visualizador de PDF
//Parámetros:
//nav: NavController para manejar la navegación
fun NavGraphBuilder.registrarGrafoPrimerosAuxilios(nav: NavController) {
    // Ruta para la pantalla principal con la lista de guías
    composable<PrimerosAuxilios> {
        PantallaPrimerosAuxilios(
            navController = nav,
            onVolver = { nav.popBackStack() }, // Regresa a la pantalla anterior
            onAbrirGuia = { nombreArchivo ->
                // Navega al visualizador PDF pasando el nombre del archivo
                nav.navigate(VisualizadorPDF(nombreArchivo = nombreArchivo))
            },
            mostrarBarraInferior = false // No muestra barra inferior en esta pantalla
        )
    }

    // Ruta para el visualizador de PDF con parámetro
    composable<VisualizadorPDF> { backStackEntry ->
        // Obtiene los argumentos de navegación de forma type-safe
        val args: VisualizadorPDF = backStackEntry.toRoute()

        // Muestra el visualizador con el archivo especificado
        PantallaVisualizadorPDF(
            nombreArchivo = args.nombreArchivo,
            onVolver = { nav.popBackStack() } // Regresa a la lista de guías
        )
    }
}