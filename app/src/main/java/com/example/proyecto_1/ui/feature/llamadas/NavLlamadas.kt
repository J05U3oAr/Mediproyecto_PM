package com.example.proyecto_1.ui.feature.llamadas


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.registrarGrafoLlamadas(nav: NavController) {
    composable<Llamadas> { ConfirmarLlamadaScreen() }
}
