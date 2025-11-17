//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.inicio

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.ui.app.Inicio
import com.example.proyecto_1.ui.feature.calendario.Calendario
import com.example.proyecto_1.ui.feature.llamadas.Llamadas
import com.example.proyecto_1.ui.feature.mapa.MapaContactos
import com.example.proyecto_1.ui.feature.primerosauxilios.PrimerosAuxilios
import com.example.proyecto_1.ui.feature.registro.Registro

//Función de extensión para NavGraphBuilder que registra el grafo de navegación del inicio
//Define todas las navegaciones posibles desde la pantalla principal
//Parámetros:
//nav: NavController para manejar la navegación entre pantallas
fun NavGraphBuilder.registrarGrafoInicio(nav: NavController) {
    //Registra la ruta composable usando el objeto serializable Inicio
    composable<Inicio> {
        //Llama a la pantalla principal pasando todas las funciones de navegación
        PantallaInicio(
            //Navega a la pantalla de primeros auxilios
            onIrPrimerosAuxilios = { nav.navigate(PrimerosAuxilios) },
            //Navega al mapa de contactos de emergencia
            onIrMapa            = { nav.navigate(MapaContactos) },
            //Navega a la pantalla de llamadas de emergencia
            onIrLlamadas        = { nav.navigate(Llamadas) },
            // Navega al calendario de recordatorios
            onIrCalendario      = { nav.navigate(Calendario) },
            // Navega al registro médico del usuario
            onIrRegistro        = { nav.navigate(Registro) },
            // Pasa el NavController para la barra inferior
            navController       = nav
        )
    }
}