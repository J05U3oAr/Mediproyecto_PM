package com.example.proyecto_1.ui.feature.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendary.R
import com.example.proyecto_1.data.AppDataManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Obtener los datos del usuario desde el gestor global
    val usuario = AppDataManager.usuarioRegistro.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            // Información del usuario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nombre
                InfoCard(
                    titulo = "Nombre",
                    valor = if (usuario.nombre.isNotBlank()) usuario.nombre else "No especificado"
                )

                // Edad
                if (usuario.edad.isNotBlank()) {
                    InfoCard(
                        titulo = "Edad",
                        valor = "${usuario.edad} años"
                    )
                }

                // Género
                if (usuario.genero.isNotBlank()) {
                    InfoCard(
                        titulo = "Género",
                        valor = usuario.genero
                    )
                }

                // Tipo de sangre
                if (usuario.tipoSangre.isNotBlank()) {
                    InfoCard(
                        titulo = "Tipo de sangre",
                        valor = usuario.tipoSangre
                    )
                }

                // Alergias
                if (usuario.alergias.isNotBlank()) {
                    InfoCard(
                        titulo = "Alergias",
                        valor = usuario.alergias
                    )
                }

                // Contacto de emergencia
                if (usuario.contactoEmergencia.isNotBlank()) {
                    InfoCard(
                        titulo = "Contacto de emergencia",
                        valor = usuario.contactoEmergencia
                    )
                }

                // Información del equipo de desarrollo
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    "Equipo de desarrollo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Carnés: 241424, 241155, 241112",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.weight(1f))

            // Botón de cerrar sesión
            Button(
                onClick = {
                    // Resetear todos los datos al cerrar sesión
                    AppDataManager.resetearDatos()
                    onCerrarSesion()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun InfoCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                valor,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}