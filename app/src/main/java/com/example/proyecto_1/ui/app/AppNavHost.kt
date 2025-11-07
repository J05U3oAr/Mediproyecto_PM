package com.example.proyecto_1.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.feature.login.PantallaAuth
import com.example.proyecto_1.ui.feature.inicio.registrarGrafoInicio
import com.example.proyecto_1.ui.feature.mapa.registrarGrafoMapa
import com.example.proyecto_1.ui.feature.llamadas.registrarGrafoLlamadas
import com.example.proyecto_1.ui.feature.registro.registrarGrafoRegistro
import com.example.proyecto_1.ui.feature.calendario.registrarGrafoCalendario
import com.example.proyecto_1.ui.feature.primerosauxilios.registrarGrafoPrimerosAuxilios
import com.example.proyecto_1.ui.feature.perfil.registrarGrafoPerfil

@Composable
fun AppNavHost(sessionManager: SessionManager) {
    val navController: NavHostController = rememberNavController()

    // Determinar la pantalla inicial según el estado de la sesión
    val startDestination: Any = when {
        !sessionManager.isLoggedIn() -> Login
        !sessionManager.isProfileCompleted() -> Registro
        else -> Inicio
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ✅ PRIMERO: Registra Login
        composable<Login> {
            PantallaAuth(
                sessionManager = sessionManager,
                onLoginExitoso = { email, nombre ->
                    // Guardar sesión
                    sessionManager.saveLoginSession(email, nombre)

                    // Navegar a Registro para completar perfil
                    navController.navigate(Registro) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ✅ LUEGO: Registra todos los demás destinos
        registrarGrafoInicio(navController)
        registrarGrafoMapa(navController)
        registrarGrafoLlamadas(navController)
        registrarGrafoRegistro(navController, sessionManager)
        registrarGrafoCalendario(navController)
        registrarGrafoPrimerosAuxilios(navController)
        registrarGrafoPerfil(navController, sessionManager)
    }
}