package com.example.proyecto_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.ui.app.AppNavHost

class MainActivity : ComponentActivity() {

    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el gestor de sesión
        sessionManager = SessionManager(this)

        setContent {
            MaterialTheme {
                AppNavHost(sessionManager = sessionManager)
            }
        }
    }
}