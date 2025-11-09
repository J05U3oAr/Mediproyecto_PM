package com.example.proyecto_1.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.data.database.AppDatabase
import com.example.proyecto_1.ui.feature.login.PantallaAuth
import com.example.proyecto_1.ui.feature.inicio.registrarGrafoInicio
import com.example.proyecto_1.ui.feature.mapa.registrarGrafoMapa
import com.example.proyecto_1.ui.feature.llamadas.registrarGrafoLlamadas
import com.example.proyecto_1.ui.feature.registro.registrarGrafoRegistro
import com.example.proyecto_1.ui.feature.calendario.registrarGrafoCalendario
import com.example.proyecto_1.ui.feature.primerosauxilios.registrarGrafoPrimerosAuxilios
import com.example.proyecto_1.ui.feature.perfil.registrarGrafoPerfil
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(sessionManager: SessionManager) {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val scope = rememberCoroutineScope()

    // Determinar la pantalla inicial según el estado de la sesión
    val startDestination: Any = when {
        !sessionManager.isLoggedIn() -> Login
        !sessionManager.hasMedicalProfile() -> Registro
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
                    // Verificar si el usuario tiene perfil médico guardado
                    scope.launch {
                        val perfilExistente = database.perfilMedicoDao().obtenerPerfilPorEmail(email)
                        val tienePerfil = perfilExistente != null

                        // Guardar sesión con información del perfil
                        sessionManager.saveLoginSession(email, nombre, tienePerfil)

                        if (tienePerfil) {
                            // Si tiene perfil, ir directo a Inicio
                            navController.navigate(Inicio) {
                                popUpTo(Login) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            // Si no tiene perfil, ir a Registro
                            navController.navigate(Registro) {
                                popUpTo(Login) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        registrarGrafoInicio(navController)
        registrarGrafoMapa(navController)
        registrarGrafoLlamadas(navController)
        registrarGrafoRegistro(navController, sessionManager)
        registrarGrafoCalendario(navController)
        registrarGrafoPrimerosAuxilios(navController)
        registrarGrafoPerfil(navController, sessionManager)
    }
}