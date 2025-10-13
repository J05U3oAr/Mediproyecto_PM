package com.example.proyecto_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.proyecto_1.ui.pantallas.PantallaMapaContactos
import com.example.proyecto_1.ui.pantallas.PantallaRegistro
import com.example.proyecto_1.ui.pantallas.PantallaMapaContactos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PantallaMapaContactos(
                    indiceSeleccionado = 0,
                    onSeleccionar = {}
                )
            }
        }
    }
}
