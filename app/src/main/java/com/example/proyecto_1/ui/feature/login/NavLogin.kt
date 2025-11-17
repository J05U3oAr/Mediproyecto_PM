//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

//Función de extensión para NavGraphBuilder que registra el grafo de navegación de login
//Nota: El registro real de la ruta se maneja en AppNavHost.kt
//Este archivo se mantiene por consistencia con la estructura del proyecto
//Parámetros:
//nav: NavController para manejar la navegación
fun NavGraphBuilder.registrarGrafoLogin(nav: NavController) {
    //No se necesita registrar Login aquí
    //Se maneja en AppNavHost.kt como pantalla inicial de autenticación
}