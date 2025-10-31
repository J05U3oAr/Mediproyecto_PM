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

@Composable
fun BarraInferior(
    navController: NavController,
    selectedRoute: String?
) {
    val c = MaterialTheme.colorScheme

    data class Item(
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val label: String,
        val dest: Any
    )

    val items = listOf(
        Item(Icons.Filled.Home, "Inicio", Inicio),
        Item(Icons.Filled.Info, "Registro", Registro),
        Item(Icons.Filled.AccountCircle, "Info", Perfil) // <- derecha: PERFIL
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = c.primary
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = selectedRoute?.contains(item.dest::class.simpleName ?: "") == true

                Column(
                    modifier = Modifier
                        .widthIn(min = 90.dp)
                        .clickable { navController.navigate(item.dest) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (selected) c.onPrimary.copy(alpha = .25f) else c.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(item.icon, contentDescription = null, tint = c.onPrimary)
                    }
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
