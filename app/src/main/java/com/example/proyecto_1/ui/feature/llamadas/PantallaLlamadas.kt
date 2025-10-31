package com.example.proyecto_1.ui.feature.llamadas

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
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¿Está seguro?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9DAF9)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(0.7f).height(80.dp)
        ) { Text("LLAMAR", fontSize = 26.sp, fontWeight = FontWeight.Medium, color = Color.Black) }

        Spacer(Modifier.height(40.dp))

        OutlinedButton(onClick = { }, shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)) {
            Text("Llamar contacto médico", fontSize = 16.sp, color = Color.Black)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = { }, shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)) {
            Text("Llamar familiar", fontSize = 16.sp, color = Color.Black)
        }
    }
}
