//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.app

import androidx.compose.runtime.Composable
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

//AppNavHost - Componente principal de navegación de la aplicación

//Responsabilidades:
//Define el grafo completo de navegación
//Determina la pantalla inicial según el estado del usuario
//Gestiona la navegación después del login
//Registra todos los destinos de la app
@Composable
fun AppNavHost(sessionManager: SessionManager) {
    // Controlador de navegación
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val scope = rememberCoroutineScope()

    //Determinar la pantalla inicial según el estado de la sesión
    //Lógica:
    //!isLoggedIn() -> Mostrar pantalla de Login
    //!hasMedicalProfile() -> Mostrar pantalla de Registro de perfil médico
    //else -> Mostrar pantalla de Inicio (todo listo)
    val startDestination: Any = when {
        !sessionManager.isLoggedIn() -> Login
        !sessionManager.hasMedicalProfile() -> Registro
        else -> Inicio
    }

    // NavHost define el contenedor de navegación
    NavHost(
        navController = navController,
        startDestination = startDestination  // Pantalla inicial
    ) {

        //Pantalla de Login/Registro de cuenta

        //Al iniciar sesión exitosamente:
        //1. Verifica si el usuario tiene perfil médico en la BD
        //2. Guarda la sesión con SessionManager
        //3. Navega a Inicio si tiene perfil, o a Registro si no
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
                                popUpTo(Login) { inclusive = true }  // Eliminar Login del back stack
                                launchSingleTop = true               // No crear múltiples instancias
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

        // Registrar todos los demás destinos de la aplicación
        registrarGrafoInicio(navController)
        registrarGrafoMapa(navController)
        registrarGrafoLlamadas(navController)
        registrarGrafoRegistro(navController, sessionManager)
        registrarGrafoCalendario(navController)
        registrarGrafoPrimerosAuxilios(navController)
        registrarGrafoPerfil(navController, sessionManager)
    }
}