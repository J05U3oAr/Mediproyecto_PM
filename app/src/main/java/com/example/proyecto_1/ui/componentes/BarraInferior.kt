//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_1.ui.app.*

//BarraInferior - Componente de navegación inferior
@Composable
fun BarraInferior(
    navController: NavController,
    selectedRoute: String?
) {
    val c = MaterialTheme.colorScheme

    //Clase de datos para representar cada item de la barra
    data class Item(
        val icon: androidx.compose.ui.graphics.vector.ImageVector,  // Icono del botón
        val label: String,                                          // Texto del botón
        val dest: Any,                                              // Destino de navegación
        val routeName: String                                       // Nombre de la ruta (para comparar)
    )

    // Definir los 3 items de la barra de navegación
    val items = listOf(
        Item(Icons.Filled.Home, "Inicio", Inicio, "Inicio"),
        Item(Icons.Filled.Info, "Registro", Registro, "Registro"),
        Item(Icons.Filled.AccountCircle, "Perfil", Perfil, "Perfil")
    )

    // Surface que contiene toda la barra
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = c.primary  // Color primario del tema
    ) {
        // Fila que distribuye los items uniformemente
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Crear un botón para cada item
            items.forEach { item ->
                // Verificar si este item está seleccionado
                val selected = selectedRoute?.contains(item.routeName, ignoreCase = true) == true

                Column(
                    modifier = Modifier
                        .widthIn(min = 90.dp)
                        .clickable {
                            // Navegar solo si no estamos ya en esa ruta
                            if (!selected) {
                                navController.navigate(item.dest) {
                                    launchSingleTop = true  // No crear múltiples instancias
                                    restoreState = true      // Restaurar estado previo
                                }
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Contenedor circular del icono
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (selected)
                                    c.onPrimary.copy(alpha = .25f)  // Fondo si está seleccionado
                                else
                                    c.primary                        // Sin fondo si no está seleccionado
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Icono
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint = c.onPrimary
                        )
                    }

                    // Texto del botón
                    Text(
                        text = item.label,
                        color = c.onPrimary,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}