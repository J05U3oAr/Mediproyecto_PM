// app/src/main/java/com/example/proyecto_1/ui/app/AppNavHost.kt
package com.example.proyecto_1.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ----- Destinos (type-safe @Serializable) -----
import com.example.proyecto_1.ui.feature.login.Login
import com.example.proyecto_1.ui.feature.inicio.Inicio

// ----- Pantallas / registro de grafos por feature -----
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
        startDestination = Login   // 👈 start con el destino @Serializable de Login
    ) {
        // ---------------- LOGIN ----------------
        composable<Login> {
            // Entrar sin validar datos: al tocar "Continuar" ir a INICIO
            PantallaAuth(
                onContinuar = {
                    navController.navigate(Inicio) {
                        // Quita Login del back stack para que "Atrás" no regrese al login
                        // Si tu versión de Navigation soporta popUpTo<T>(), usa esta:
                        popUpTo<Login> { inclusive = true }

                        // Si te marcara error esa línea, usa este Fallback:
                        // popUpTo(navController.graph.startDestinationId) { inclusive = true }

                        launchSingleTop = true
                    }
                }
            )
        }

        // --------- FEATURES (registra TODOS tus subgrafos) ----------
        registrarGrafoInicio(navController)           // incluye PantallaInicio
        registrarGrafoMapa(navController)
        registrarGrafoLlamadas(navController)
        registrarGrafoRegistro(navController)
        registrarGrafoCalendario(navController)
        registrarGrafoPrimerosAuxilios(navController)
        registrarGrafoPerfil(navController)           // tu pantalla de perfil
    }
}
