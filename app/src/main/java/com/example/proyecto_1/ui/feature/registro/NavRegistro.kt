package com.example.proyecto_1.ui.feature.registro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registrarGrafoRegistro(nav: NavController) {
    composable<Registro> { PantallaRegistro() }
}
