package com.example.proyecto_1.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmarLlamadaScreen() {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF4EEFB)
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Navegar a Home */ },
                    label = { Text("Home") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Navegar a historial */ },
                    label = { Text("Historial médico") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Navegar a info */ },
                    label = { Text("Info") },
                    icon = {}
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título principal
            Text(
                text = "¿Está seguro?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Botón principal de llamada
            Button(
                onClick = { /* Sin lógica */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE9DAF9)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(80.dp)
            ) {
                Text(
                    text = "LLAMAR",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón para contacto médico
            OutlinedButton(
                onClick = { /* Sin lógica */ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Llamar contacto médico",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para familiar
            OutlinedButton(
                onClick = { /* Sin lógica */ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Llamar familiar",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}