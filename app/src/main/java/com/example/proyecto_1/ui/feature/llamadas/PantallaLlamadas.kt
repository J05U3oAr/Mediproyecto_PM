//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.llamadas

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_1.data.AppDataManager

//Muestra el contacto de emergencia configurado y solicita confirmación antes de llamar
@Composable
fun ConfirmarLlamadaScreen(
    onVolverInicio: () -> Unit = {},          // Acción para regresar al menú principal
    viewModel: LlamadasViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current
    val usuario = AppDataManager.usuarioRegistro.value
    val uiState by viewModel.uiState.collectAsState()

    // Launcher para solicitar permiso de llamada
    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el permiso fue otorgado, realizar la llamada al contacto de emergencia
            if (usuario.contactoEmergenciaNumero.isNotBlank()) {
                try {
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${usuario.contactoEmergenciaNumero}")
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error al realizar la llamada: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "No hay contacto de emergencia registrado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Permiso de llamada denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            // Pantalla de carga mientras el ViewModel simula carga inicial
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cs.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = cs.primary,
                        strokeWidth = 6.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Cargando...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = cs.onBackground
                    )
                }
            }
        } else {
            // Contenido principal: confirmación de llamada y botones de acción
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cs.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título de confirmación
                Text(
                    text = "¿Está seguro?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = cs.onBackground,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Card con información del contacto de emergencia, si existe
                if (usuario.contactoEmergenciaNombre.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cs.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Se llamará a:",
                                fontSize = 14.sp,
                                color = cs.onPrimaryContainer
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                usuario.contactoEmergenciaNombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = cs.onPrimaryContainer
                            )
                            Text(
                                usuario.contactoEmergenciaNumero,
                                fontSize = 16.sp,
                                color = cs.onPrimaryContainer
                            )
                        }
                    }
                }

                // Botón principal para iniciar la llamada (solicita permiso si es necesario)
                Button(
                    onClick = {
                        if (usuario.contactoEmergenciaNumero.isNotBlank()) {
                            // Solicitar permiso de llamada antes de marcar
                            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        } else {
                            Toast.makeText(
                                context,
                                "No hay contacto de emergencia registrado. Por favor configúralo en Registro.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.secondaryContainer,
                        contentColor = cs.onSecondaryContainer
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(80.dp)
                ) {
                    Text(
                        "LLAMAR",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(40.dp))

                // Botón secundario: regresar al menú principal sin llamar
                OutlinedButton(
                    onClick = onVolverInicio,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = cs.primary
                    )
                ) {
                    Text(
                        "Regresar al menú principal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}