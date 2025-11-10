package com.example.proyecto_1.ui.feature.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto_1.ui.componentes.BarraInferior
import com.example.calendary.R

@Composable
fun PantallaInicio(
    onIrPrimerosAuxilios: () -> Unit,
    onIrMapa: () -> Unit,
    onIrLlamadas: () -> Unit,
    onIrCalendario: () -> Unit,
    onIrRegistro: () -> Unit,
    navController: NavController
) {
    val colores = MaterialTheme.colorScheme
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BarraInferior(
                navController = navController,
                selectedRoute = rutaActual
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colores.background)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("HusL", fontSize = 34.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))


            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaInicio(
                    titulo = "Primero Auxilios",
                    imagen = R.drawable.fondo_primerosaux,
                    onClick = onIrPrimerosAuxilios,
                    modifier = Modifier.weight(1f)
                )
                TarjetaInicio(
                    titulo = "Mapa de contactos",
                    imagen = R.drawable.mapa_contactos,
                    onClick = onIrMapa,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                TarjetaInicio(
                    titulo = "Notificaciones",
                    imagen = R.drawable.notificaciones,
                    onClick = onIrCalendario,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onIrLlamadas,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.secondaryContainer,
                    contentColor = colores.onSecondaryContainer
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "🚑 Emergencia", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun TarjetaInicio(
    titulo: String,
    imagen: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F1FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imagen),
                contentDescription = titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
