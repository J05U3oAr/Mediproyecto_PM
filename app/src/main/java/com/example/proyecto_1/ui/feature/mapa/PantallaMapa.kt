package com.example.proyecto_1.ui.feature.mapa

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.ContactoEmergencia
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapaContactos(onVolver: () -> Unit = {}) {
    val c = MaterialTheme.colorScheme
    val context = LocalContext.current

    var showAgregarDialog by remember { mutableStateOf(false) }
    var showAlertaDialog by remember { mutableStateOf(false) }
    var contactoSeleccionado by remember { mutableStateOf<ContactoEmergencia?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }

    // Usar los contactos del gestor global
    val contactos = AppDataManager.contactos
    val usuario = AppDataManager.usuarioRegistro.value

    // Launcher para permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (locationPermissionGranted) {
            // Obtener ubicación
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                Toast.makeText(context, "Error al obtener ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Launcher para permiso de llamada
    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            contactoSeleccionado?.let { contacto ->
                try {
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${contacto.telefono}")
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error al realizar la llamada: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Solicitar permisos al iniciar
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

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

            // Mapa real con Google Maps
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                if (locationPermissionGranted && currentLocation != null) {
                    AndroidView(
                        factory = { ctx ->
                            MapView(ctx).apply {
                                onCreate(null)
                                onResume()
                                getMapAsync { googleMap ->
                                    currentLocation?.let { location ->
                                        googleMap.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(location, 15f)
                                        )
                                        googleMap.addMarker(
                                            MarkerOptions()
                                                .position(location)
                                                .title("Tu ubicación")
                                        )
                                    }
                                    googleMap.uiSettings.isZoomControlsEnabled = true
                                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                                    try {
                                        googleMap.isMyLocationEnabled = true
                                    } catch (e: SecurityException) {
                                        // Ignorar si no hay permisos
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Obteniendo ubicación...",
                                color = c.onSurfaceVariant
                            )
                        }
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
                // Incluir el contacto de emergencia del registro
                if (usuario.contactoEmergenciaNombre.isNotBlank() && usuario.contactoEmergenciaNumero.isNotBlank()) {
                    item {
                        ItemContacto(
                            contacto = ContactoEmergencia(
                                id = 0,
                                nombre = usuario.contactoEmergenciaNombre,
                                telefono = usuario.contactoEmergenciaNumero,
                                relacion = "Contacto principal"
                            ),
                            onClick = {
                                contactoSeleccionado = ContactoEmergencia(
                                    id = 0,
                                    nombre = usuario.contactoEmergenciaNombre,
                                    telefono = usuario.contactoEmergenciaNumero,
                                    relacion = "Contacto principal"
                                )
                            },
                            onLlamar = {
                                contactoSeleccionado = ContactoEmergencia(
                                    id = 0,
                                    nombre = usuario.contactoEmergenciaNombre,
                                    telefono = usuario.contactoEmergenciaNumero,
                                    relacion = "Contacto principal"
                                )
                                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        )
                    }
                }

                items(contactos.toList()) { contacto ->
                    ItemContacto(
                        contacto = contacto,
                        onClick = { contactoSeleccionado = contacto },
                        onLlamar = {
                            contactoSeleccionado = contacto
                            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
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
            title = { Text("Enviar Alerta de Emergencia", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Se enviará un mensaje de WhatsApp con el texto 'emergencia' y tu ubicación a todos tus contactos de emergencia.",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (currentLocation != null) {
                        Text(
                            "Ubicación: ${currentLocation!!.latitude}, ${currentLocation!!.longitude}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Enviar mensaje por WhatsApp a todos los contactos
                        val ubicacionTexto = if (currentLocation != null) {
                            "\nUbicación: https://maps.google.com/?q=${currentLocation!!.latitude},${currentLocation!!.longitude}"
                        } else {
                            ""
                        }

                        // Lista de todos los contactos incluyendo el principal
                        val todosContactos = mutableListOf<ContactoEmergencia>()
                        if (usuario.contactoEmergenciaNombre.isNotBlank() && usuario.contactoEmergenciaNumero.isNotBlank()) {
                            todosContactos.add(
                                ContactoEmergencia(
                                    0,
                                    usuario.contactoEmergenciaNombre,
                                    usuario.contactoEmergenciaNumero,
                                    "Principal"
                                )
                            )
                        }
                        todosContactos.addAll(contactos)

                        if (todosContactos.isEmpty()) {
                            Toast.makeText(
                                context,
                                "No hay contactos de emergencia configurados",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            todosContactos.forEach { contacto ->
                                try {
                                    val numeroLimpio = contacto.telefono.replace("[^0-9+]".toRegex(), "")
                                    val mensaje = "🚨 EMERGENCIA 🚨$ubicacionTexto"
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://wa.me/$numeroLimpio?text=${Uri.encode(mensaje)}")
                                        setPackage("com.whatsapp")
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error al enviar mensaje a ${contacto.nombre}. Verifica que WhatsApp esté instalado.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            Toast.makeText(
                                context,
                                "Abriendo WhatsApp para ${todosContactos.size} contacto(s)...",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        showAlertaDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Text("Enviar Alerta")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAlertaDialog = false }) {
                    Text("Cancelar")
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
                        callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
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
                    onValueChange = {
                        if (it.all { char -> char.isDigit() || char == '+' }) {
                            telefono = it
                        }
                    },
                    label = { Text("Teléfono") },
                    placeholder = { Text("Ej: +50212345678") },
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
    onClick: () -> Unit,
    onLlamar: () -> Unit = {}
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onLlamar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Llamar")
                }
                OutlinedButton(
                    onClick = onClick
                ) {
                    Text("Ver")
                }
            }
        }
    }
}