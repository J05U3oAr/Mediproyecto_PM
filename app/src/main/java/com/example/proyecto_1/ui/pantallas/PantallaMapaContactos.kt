package com.example.proyecto_1.ui.pantallas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PantallaMapaContactos(
    indiceSeleccionado: Int,
    onSeleccionar: (Int) -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colores.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colores.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mapa de contactos",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = colores.onSecondaryContainer
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colores.surfaceVariant)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width; val h = size.height
                    val colorPin = colores.primary
                    val puntos = listOf(
                        0.15f to 0.25f, 0.35f to 0.35f, 0.55f to 0.20f,
                        0.72f to 0.45f, 0.25f to 0.60f, 0.50f to 0.70f, 0.80f to 0.30f
                    )
                    puntos.forEach { (px, py) ->
                        drawCircle(Color.White, radius = 16f, center = Offset(w * px, h * py))
                        drawCircle(colorPin, radius = 8f, center = Offset(w * px, h * py))
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonAccion(texto = "Enviar alerta", modifier = Modifier.weight(1f))
                BotonAccion(texto = "Agregar contacto", modifier = Modifier.weight(1f))
            }

            // Contactos
            ItemContacto("Contacto 1")
            ItemContacto("Contacto 2")
            ItemContacto("Contacto 3")
        }

        BarraInferior(
            indiceSeleccionado = indiceSeleccionado,
            onSeleccionar = onSeleccionar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}


@Composable
private fun BotonAccion(texto: String, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    Button(
        onClick = {},
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = c.secondaryContainer,
            contentColor = c.onSecondaryContainer
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(
            text = texto,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ItemContacto(texto: String) {
    OutlinedButton(
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text("  $texto", color = MaterialTheme.colorScheme.primary)
    }
}


@Composable
fun BarraInferior(
    indiceSeleccionado: Int,
    onSeleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = MaterialTheme.colorScheme
    val items = listOf(
        ItemBarra(icono = Icons.Filled.Home, texto = "Inicio"),
        ItemBarra(icono = Icons.Filled.Info, texto = "Historial médico"),
        ItemBarra(icono = Icons.Filled.AccountCircle, texto = "Información")
    )

    Surface(
        modifier = modifier
            .height(80.dp),
        color = c.secondaryContainer,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                ElementoBarra(
                    icono = item.icono,
                    texto = item.texto,
                    seleccionado = (index == indiceSeleccionado),
                    onClick = { onSeleccionar(index) }
                )
            }
        }
    }
}

private data class ItemBarra(val icono: ImageVector, val texto: String)

@Composable
private fun ElementoBarra(
    icono: ImageVector,
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val c = MaterialTheme.colorScheme
    val colorContenido = c.onSecondaryContainer
    val colorHalo = c.onSecondaryContainer.copy(alpha = 0.25f)

    Column(
        modifier = Modifier
            .widthIn(min = 96.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (seleccionado) colorHalo else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icono, contentDescription = texto, tint = colorContenido)
        }
        Text(
            text = texto,
            color = colorContenido,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VistaPreviaMapa() {
    val (indice, setIndice) = remember { mutableStateOf(0) }
    MaterialTheme {
        PantallaMapaContactos(indice, setIndice)
    }
}
