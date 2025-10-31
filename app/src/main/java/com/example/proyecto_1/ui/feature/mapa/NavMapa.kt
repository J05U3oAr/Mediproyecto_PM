package com.example.proyecto_1.ui.feature.mapa

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registrarGrafoMapa(nav: NavController) {
    composable<MapaContactos> { PantallaMapaContactos() }
}
