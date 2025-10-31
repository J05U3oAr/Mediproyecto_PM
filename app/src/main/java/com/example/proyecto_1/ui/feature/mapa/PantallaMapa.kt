package com.example.proyecto_1.ui.feature.mapa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaMapaContactos() {
    val c = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(64.dp)
                .clip(RoundedCornerShape(16.dp)).background(c.secondaryContainer),
            contentAlignment = Alignment.Center
        ) { Text("Mapa de contactos", fontSize = 28.sp, color = c.onSecondaryContainer) }

        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp)
                .clip(RoundedCornerShape(16.dp)).background(c.surfaceVariant)
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val w = size.width; val h = size.height; val pin = c.primary
                listOf(0.15f to .25f, .35f to .35f, .55f to .20f, .72f to .45f, .25f to .60f, .50f to .70f, .80f to .30f)
                    .forEach { (x, y) ->
                        drawCircle(Color.White, radius = 16f, center = Offset(w*x, h*y))
                        drawCircle(pin, radius = 8f, center = Offset(w*x, h*y))
                    }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            BotonAccion("Enviar alerta", Modifier.weight(1f))
            BotonAccion("Agregar contacto", Modifier.weight(1f))
        }

        ItemContacto("Contacto 1")
        ItemContacto("Contacto 2")
        ItemContacto("Contacto 3")
    }
}

@Composable private fun BotonAccion(texto: String, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    Button(onClick = {}, shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(c.secondaryContainer, c.onSecondaryContainer),
        modifier = modifier.height(56.dp)
    ) { Text(texto) }
}

@Composable private fun ItemContacto(texto: String) {
    OutlinedButton(onClick = {}, shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(52.dp)
    ) { Text(texto, color = MaterialTheme.colorScheme.primary) }
}
