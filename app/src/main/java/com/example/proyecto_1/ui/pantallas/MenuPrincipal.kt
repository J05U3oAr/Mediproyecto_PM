package com.example.proyecto_1.ui.pantallas


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = { BottomMenu() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("HusL", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { /* Perfil */ }) {
                    AsyncImage(
                        model = "https://e7.pngegg.com/pngimages/518/64/png-clipart-person-icon-computer-icons-user-profile-symbol-person-miscellaneous-monochrome-thumbnail.png",
                        contentDescription = "Perfil",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuCard("Mis Medicamentos", "https://cdn-icons-png.flaticon.com/512/918/918330.png") { }
                MenuCard("Agregar Recordatorio", "https://cdn-icons-png.flaticon.com/512/1976/1976848.png") { }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuCard("Notificaciones", "https://cdn-icons-png.flaticon.com/512/4658/4658755.png") { }
                MenuCard("Emergencia", "https://cdn-icons-png.flaticon.com/512/1976/1976848.png") { }
            }
        }
    }
}

@Composable
fun MenuCard(title: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun BottomMenu() {
    NavigationBar(containerColor = Color(0xFFEAEAEA)) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
                    contentDescription = "Inicio",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/32/32284.png",
                    contentDescription = "Historial",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Historial") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/32/32175.png",
                    contentDescription = "Info",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Info") }
        )
    }
}
