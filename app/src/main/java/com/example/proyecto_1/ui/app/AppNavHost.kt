package com.example.proyecto_1.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.proyecto_1.ui.feature.login.Login
import com.example.proyecto_1.ui.feature.inicio.Inicio

import com.example.proyecto_1.ui.feature.login.PantallaAuth
import com.example.proyecto_1.ui.feature.inicio.registrarGrafoInicio
import com.example.proyecto_1.ui.feature.mapa.registrarGrafoMapa
import com.example.proyecto_1.ui.feature.llamadas.registrarGrafoLlamadas
import com.example.proyecto_1.ui.feature.registro.registrarGrafoRegistro
import com.example.proyecto_1.ui.feature.calendario.registrarGrafoCalendario
import com.example.proyecto_1.ui.feature.primerosauxilios.registrarGrafoPrimerosAuxilios
import com.example.proyecto_1.ui.feature.perfil.registrarGrafoPerfil

@Composable
fun AppNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {

            PantallaAuth(
                onContinuar = {
                    navController.navigate(Inicio) {

                        popUpTo<Login> { inclusive = true }

                        launchSingleTop = true
                    }
                }
            )
        }

        registrarGrafoInicio(navController)
        registrarGrafoMapa(navController)
        registrarGrafoLlamadas(navController)
        registrarGrafoRegistro(navController)
        registrarGrafoCalendario(navController)
        registrarGrafoPrimerosAuxilios(navController)
        registrarGrafoPerfil(navController)
    }
}
