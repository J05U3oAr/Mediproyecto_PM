//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.app.AppNavHost
import com.example.proyecto_1.ui.theme.Proyecto_1Theme

//Actividad principal de la aplicación HusL
class MainActivity : ComponentActivity() {

    // Gestor de sesión del usuario (login, perfil, estado)
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar edge-to-edge para mejor integración con el sistema
        enableEdgeToEdge()

        // Inicializar el gestor de sesión
        sessionManager = SessionManager(this)

        // Establecer el contenido de la UI con Compose
        setContent {
            // Aplicar el tema de la app con soporte para modo oscuro
            Proyecto_1Theme {
                // AppNavHost maneja toda la navegación de la app
                AppNavHost(sessionManager = sessionManager)
            }
        }
    }
}