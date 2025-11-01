package com.example.proyecto_1.ui.feature.mapa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.ContactoEmergencia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapaContactos(onVolver: () -> Unit = {}) {
    val c = MaterialTheme.colorScheme
    var showAgregarDialog by remember { mutableStateOf(false) }
    var showAlertaDialog by remember { mutableStateOf(false) }
    var contactoSeleccionado by remember { mutableStateOf<ContactoEmergencia?>(null) }

    // Usar los contactos del gestor global
    val contactos = AppDataManager.contactos

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mapa de contactos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.secondaryContainer,
                    titleContentColor = c.onSecondaryContainer,
                    navigationIconContentColor = c.onSecondaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Mapa con pins de contactos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(c.surfaceVariant)
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val pin = c.primary

                    // Posiciones de los contactos en el mapa
                    val positions = listOf(
                        0.15f to 0.25f,
                        0.35f to 0.35f,
                        0.55f to 0.20f,
                        0.72f to 0.45f,
                        0.25f to 0.60f,
                        0.50f to 0.70f,
                        0.80f to 0.30f
                    )

                    positions.take(contactos.size).forEach { (x, y) ->
                        // Círculo blanco de fondo
                        drawCircle(
                            Color.White,
                            radius = 16f,
                            center = Offset(w * x, h * y)
                        )
                        // Pin de color
                        drawCircle(
                            pin,
                            radius = 8f,
                            center = Offset(w * x, h * y)
                        )
                    }
                }
            }

            // Botones de acción
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonAccion(
                    texto = "Enviar alerta",
                    icono = Icons.Default.Warning,
                    onClick = { showAlertaDialog = true },
                    modifier = Modifier.weight(1f)
                )
                BotonAccion(
                    texto = "Agregar contacto",
                    icono = Icons.Default.PersonAdd,
                    onClick = { showAgregarDialog = true },
                    modifier = Modifier.weight(1f)
                )
            }

            // Lista de contactos
            Text(
                "Contactos de emergencia",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = c.onSurface
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contactos.toList()) { contacto ->
                    ItemContacto(
                        contacto = contacto,
                        onClick = { contactoSeleccionado = contacto }
                    )
                }
            }
        }
    }

    // Diálogo para agregar contacto
    if (showAgregarDialog) {
        AgregarContactoDialog(
            onDismiss = { showAgregarDialog = false },
            onConfirm = { nuevoContacto ->
                AppDataManager.agregarContacto(nuevoContacto)
                showAgregarDialog = false
            }
        )
    }

    // Diálogo de alerta enviada
    if (showAlertaDialog) {
        AlertDialog(
            onDismissRequest = { showAlertaDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFF44336)
                )
            },
            title = { Text("Alerta Enviada", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Se ha enviado una alerta de emergencia a todos tus contactos con tu ubicación actual.",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showAlertaDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Text("Entendido")
                }
            }
        )
    }

    // Diálogo de información del contacto
    if (contactoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { contactoSeleccionado = null },
            title = {
                Text(
                    contactoSeleccionado!!.nombre,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Teléfono: ${contactoSeleccionado!!.telefono}")
                    Text("Relación: ${contactoSeleccionado!!.relacion}")
                }
            },
            confirmButton = {
                Button(
                    onClick = { contactoSeleccionado = null }
                ) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Aquí iría la lógica para llamar
                        contactoSeleccionado = null
                    }
                ) {
                    Text("Llamar")
                }
            }
        )
    }
}

@Composable
fun AgregarContactoDialog(
    onDismiss: () -> Unit,
    onConfirm: (ContactoEmergencia) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var relacion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Agregar Contacto",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    placeholder = { Text("Ej: Dr. Juan Pérez") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    placeholder = { Text("Ej: 5551-2345") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = relacion,
                    onValueChange = { relacion = it },
                    label = { Text("Relación") },
                    placeholder = { Text("Ej: Familiar, Médico, Amigo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && telefono.isNotBlank() && relacion.isNotBlank()) {
                        val nuevoId = (1..10000).random()
                        val nuevoContacto = ContactoEmergencia(
                            id = nuevoId,
                            nombre = nombre,
                            telefono = telefono,
                            relacion = relacion
                        )
                        onConfirm(nuevoContacto)
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun BotonAccion(
    texto: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = c.secondaryContainer,
            contentColor = c.onSecondaryContainer
        ),
        modifier = modifier.height(56.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(texto, fontSize = 14.sp)
    }
}

@Composable
private fun ItemContacto(
    contacto: ContactoEmergencia,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    contacto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    contacto.relacion,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Ver")
            }
        }
    }
}