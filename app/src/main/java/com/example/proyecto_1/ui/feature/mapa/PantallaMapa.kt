//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.mapa

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.ContactoEmergencia
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

//Incluye nombre, dirección, coordenadas, tipo y distancia desde la ubicación actual
data class LugarCercano(
    val nombre: String,
    val direccion: String,
    val latLng: LatLng,
    val tipo: String,          // "hospital" o "farmacia"
    val distancia: Float = 0f  // Distancia en km desde la ubicación actual
)

@OptIn(ExperimentalMaterial3Api::class)
//Muestra la ubicación actual en el mapa, hospitales/farmacias cercanas y lista de contactos de emergencia
@Composable
fun PantallaMapaContactos(
    onVolver: () -> Unit = {},          // Acción para regresar a la pantalla anterior
    viewModel: MapaViewModel = viewModel()
) {
    val c = MaterialTheme.colorScheme
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Estados de UI
    var showAgregarDialog by remember { mutableStateOf(false) }
    var showAlertaDialog by remember { mutableStateOf(false) }
    var contactoSeleccionado by remember { mutableStateOf<ContactoEmergencia?>(null) }
    var contactoParaLlamar by remember { mutableStateOf<ContactoEmergencia?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var lugaresCercanos by remember { mutableStateOf<List<LugarCercano>>(emptyList()) }
    var mostrarHospitales by remember { mutableStateOf(true) }
    var mostrarFarmacias by remember { mutableStateOf(true) }
    var buscandoLugares by remember { mutableStateOf(false) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    // Datos globales: contactos y perfil de usuario
    val contactos = AppDataManager.contactos
    val usuario = AppDataManager.usuarioRegistro.value

    // Launcher para permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (locationPermissionGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location ->
                    location?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                Toast.makeText(context, "Error al obtener ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Launcher para permiso de llamada telefónica
    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            contactoParaLlamar?.let { contacto ->
                try {
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${contacto.telefono}")
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al realizar la llamada", Toast.LENGTH_SHORT).show()
                }
                contactoParaLlamar = null
            }
        } else {
            Toast.makeText(context, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
            contactoParaLlamar = null
        }
    }

    // LaunchedEffect - Solicita permisos de ubicación al entrar a la pantalla
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // LaunchedEffect - Busca lugares cercanos (hospitales/farmacias) cada vez que:
    // 1) Cambia la ubicación
    // 2) Cambia la selección de filtros (mostrarHospitales/mostrarFarmacias)
    LaunchedEffect(currentLocation, mostrarHospitales, mostrarFarmacias) {
        currentLocation?.let { ubicacion ->
            if (mostrarHospitales || mostrarFarmacias) {
                buscandoLugares = true
                withContext(Dispatchers.IO) {
                    try {
                        val lugares = mutableListOf<LugarCercano>()

                        val API_KEY = "AIzaSyC_TU_API_KEY_REAL_AQUI"

                        if (mostrarHospitales) {
                            val hospitales = buscarLugaresCercanos(
                                ubicacion,
                                "hospital",
                                API_KEY
                            )
                            lugares.addAll(hospitales)
                        }

                        if (mostrarFarmacias) {
                            val farmacias = buscarLugaresCercanos(
                                ubicacion,
                                "pharmacy",
                                API_KEY
                            )
                            lugares.addAll(farmacias)
                        }

                        withContext(Dispatchers.Main) {
                            lugaresCercanos = lugares.sortedBy { it.distancia }
                            buscandoLugares = false
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error al buscar lugares", Toast.LENGTH_SHORT).show()
                            buscandoLugares = false
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        // Barra superior con título y botón de regreso
        topBar = {
            TopAppBar(
                title = { Text("Mapa de contactos", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, "Volver")
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                // Pantalla de carga mientras el ViewModel prepara datos
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(64.dp), color = c.primary, strokeWidth = 6.dp)
                        Spacer(Modifier.height(16.dp))
                        Text("Cargando...", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(Modifier.height(8.dp))

                    Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                        if (locationPermissionGranted && currentLocation != null) {
                            // AndroidView con MapView de Google Maps
                            AndroidView(
                                factory = { ctx ->
                                    MapView(ctx).apply {
                                        onCreate(null)
                                        onResume()
                                        mapView = this
                                        getMapAsync { googleMap ->
                                            currentLocation?.let { location ->
                                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
                                                // Marcador de la ubicación del usuario
                                                googleMap.addMarker(
                                                    MarkerOptions()
                                                        .position(location)
                                                        .title("Tu ubicación")
                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                                )
                                                googleMap.uiSettings.isZoomControlsEnabled = true
                                                googleMap.uiSettings.isMyLocationButtonEnabled = true
                                                try {
                                                    googleMap.isMyLocationEnabled = true
                                                } catch (e: SecurityException) {}
                                            }
                                        }
                                    }
                                },
                                update = { view ->
                                    view.getMapAsync { googleMap ->
                                        googleMap.clear()
                                        // Refrescar marcadores cada vez que cambien lugaresCercanos o ubicación
                                        currentLocation?.let { location ->
                                            googleMap.addMarker(
                                                MarkerOptions()
                                                    .position(location)
                                                    .title("Tu ubicación")
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                            )
                                            lugaresCercanos.forEach { lugar ->
                                                val color = when (lugar.tipo) {
                                                    "hospital" -> BitmapDescriptorFactory.HUE_RED
                                                    "farmacia" -> BitmapDescriptorFactory.HUE_GREEN
                                                    else -> BitmapDescriptorFactory.HUE_ORANGE
                                                }
                                                googleMap.addMarker(
                                                    MarkerOptions()
                                                        .position(lugar.latLng)
                                                        .title(lugar.nombre)
                                                        .snippet(lugar.direccion)
                                                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                                                )
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Mensaje mientras se obtiene la ubicación o sin permisos
                            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator()
                                    Spacer(Modifier.height(8.dp))
                                    Text("Obteniendo ubicación...", color = c.onSurfaceVariant)
                                }
                            }
                        }
                    }

                    // Filtros de hospitales/farmacias para el mapa
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = c.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Mostrar en el mapa:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                FilterChip(
                                    selected = mostrarHospitales,
                                    onClick = { mostrarHospitales = !mostrarHospitales },
                                    label = { Text("🏥 Hospitales") },
                                    leadingIcon = if (mostrarHospitales) {
                                        { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                                    } else null
                                )
                                FilterChip(
                                    selected = mostrarFarmacias,
                                    onClick = { mostrarFarmacias = !mostrarFarmacias },
                                    label = { Text("💊 Farmacias") },
                                    leadingIcon = if (mostrarFarmacias) {
                                        { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                                    } else null
                                )
                            }
                        }
                    }

                    // Botones principales: enviar alerta y agregar contacto
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        BotonAccion("Enviar alerta", Icons.Default.Warning, { showAlertaDialog = true }, Modifier.weight(1f))
                        BotonAccion("Agregar contacto", Icons.Default.PersonAdd, { showAgregarDialog = true }, Modifier.weight(1f))
                    }

                    // Si hay lugares cercanos, se muestra la lista de hospitales/farmacias
                    if (lugaresCercanos.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lugares cercanos (${lugaresCercanos.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = c.onSurface)
                            if (buscandoLugares) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            }
                        }

                        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(lugaresCercanos.take(10)) { lugar ->
                                ItemLugarCercano(lugar) {
                                    // Abrir Google Maps con la ubicación del lugar
                                    val uri = "geo:${lugar.latLng.latitude},${lugar.latLng.longitude}?q=${lugar.latLng.latitude},${lugar.latLng.longitude}(${lugar.nombre})"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                    intent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(intent)
                                }
                            }
                        }
                    } else {
                        // Si no hay lugares, mostrar contactos de emergencia
                        Text("Contactos de emergencia", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = c.onSurface)

                        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Contacto principal del perfil médico
                            if (usuario.contactoEmergenciaNombre.isNotBlank() && usuario.contactoEmergenciaNumero.isNotBlank()) {
                                item {
                                    ItemContacto(
                                        contacto = ContactoEmergencia(0, usuario.contactoEmergenciaNombre, usuario.contactoEmergenciaNumero, "Contacto principal"),
                                        onClick = { contactoSeleccionado = ContactoEmergencia(0, usuario.contactoEmergenciaNombre, usuario.contactoEmergenciaNumero, "Contacto principal") },
                                        onLlamar = {
                                            contactoParaLlamar = ContactoEmergencia(0, usuario.contactoEmergenciaNombre, usuario.contactoEmergenciaNumero, "Contacto principal")
                                            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                                        }
                                    )
                                }
                            }
                            // Lista de contactos adicionales guardados en AppDataManager
                            items(contactos.toList()) { contacto ->
                                ItemContacto(
                                    contacto = contacto,
                                    onClick = { contactoSeleccionado = contacto },
                                    onLlamar = {
                                        contactoParaLlamar = contacto
                                        callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para agregar un nuevo contacto de emergencia
    if (showAgregarDialog) {
        AgregarContactoDialog(
            onDismiss = { showAgregarDialog = false },
            onConfirm = { nuevoContacto ->
                AppDataManager.agregarContacto(nuevoContacto)
                showAgregarDialog = false
            }
        )
    }

    // Diálogo de confirmación para enviar alerta de emergencia vía WhatsApp
    if (showAlertaDialog) {
        AlertDialog(
            onDismissRequest = { showAlertaDialog = false },
            icon = { Icon(Icons.Default.Warning, null, tint = Color(0xFFF44336)) },
            title = { Text("Enviar Alerta de Emergencia", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Se enviará un mensaje de WhatsApp con 'emergencia' y tu ubicación a todos tus contactos.", Modifier.padding(bottom = 8.dp))
                    if (currentLocation != null) {
                        Text("Ubicación: ${currentLocation!!.latitude}, ${currentLocation!!.longitude}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val ubicacionTexto = if (currentLocation != null) {
                            "\nUbicación: https://maps.google.com/?q=${currentLocation!!.latitude},${currentLocation!!.longitude}"
                        } else ""

                        // Construir lista de todos los contactos (principal + adicionales)
                        val todosContactos = mutableListOf<ContactoEmergencia>()
                        if (usuario.contactoEmergenciaNombre.isNotBlank() && usuario.contactoEmergenciaNumero.isNotBlank()) {
                            todosContactos.add(ContactoEmergencia(0, usuario.contactoEmergenciaNombre, usuario.contactoEmergenciaNumero, "Principal"))
                        }
                        todosContactos.addAll(contactos)

                        if (todosContactos.isEmpty()) {
                            Toast.makeText(context, "No hay contactos configurados", Toast.LENGTH_LONG).show()
                        } else {
                            // abrir whatsapp y enviar mensaje
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
                                    Toast.makeText(context, "Error al enviar a ${contacto.nombre}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            Toast.makeText(context, "Abriendo WhatsApp para ${todosContactos.size} contacto(s)...", Toast.LENGTH_LONG).show()
                        }
                        showAlertaDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) { Text("Enviar Alerta") }
            },
            dismissButton = { TextButton(onClick = { showAlertaDialog = false }) { Text("Cancelar") } }
        )
    }

    // Diálogo de detalle de contacto seleccionado (ver datos + opción de llamar)
    if (contactoSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { contactoSeleccionado = null },
            title = { Text(contactoSeleccionado!!.nombre, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Teléfono: ${contactoSeleccionado!!.telefono}")
                    Text("Relación: ${contactoSeleccionado!!.relacion}")
                }
            },
            confirmButton = { Button(onClick = { contactoSeleccionado = null }) { Text("Cerrar") } },
            dismissButton = {
                TextButton(onClick = {
                    contactoParaLlamar = contactoSeleccionado
                    contactoSeleccionado = null
                    callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }) { Text("Llamar") }
            }
        )
    }
}

//buscarLugaresCercanos - Consulta la API de Google Places para encontrar lugares cercanos
//Recibe la ubicación, tipo de lugar (hospital/pharmacy), API key y radio de búsqueda en metros
suspend fun buscarLugaresCercanos(ubicacion: LatLng, tipo: String, apiKey: String, radio: Int = 5000): List<LugarCercano> {
    return try {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=${ubicacion.latitude},${ubicacion.longitude}" +
                "&radius=$radio&type=$tipo&key=$apiKey"
        val response = URL(url).readText()
        val json = JSONObject(response)
        val results = json.getJSONArray("results")
        val lugares = mutableListOf<LugarCercano>()
        for (i in 0 until results.length().coerceAtMost(10)) {
            val place = results.getJSONObject(i)
            val location = place.getJSONObject("geometry").getJSONObject("location")
            val lat = location.getDouble("lat")
            val lng = location.getDouble("lng")
            val distancia = calcularDistancia(ubicacion.latitude, ubicacion.longitude, lat, lng)
            lugares.add(
                LugarCercano(
                    nombre = place.getString("name"),
                    direccion = place.optString("vicinity", "Dirección no disponible"),
                    latLng = LatLng(lat, lng),
                    tipo = if (tipo == "hospital") "hospital" else "farmacia",
                    distancia = distancia
                )
            )
        }
        lugares
    } catch (e: Exception) {
        emptyList()
    }
}

//calcularDistancia - Calcula la distancia entre dos puntos geográficos en km
//Utiliza Location.distanceBetween y devuelve la distancia en kilómetros
fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0] / 1000f
}

//ItemLugarCercano - Tarjeta para mostrar información de un lugar cercano (hospital/farmacia)
//Incluye nombre, dirección, distancia y botón para abrir en Google Maps
@Composable
fun ItemLugarCercano(lugar: LugarCercano, onAbrirMapa: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (lugar.tipo == "hospital") "🏥" else "💊", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(lugar.nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(Modifier.height(4.dp))
                Text(lugar.direccion, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Text("📍 ${String.format("%.2f", lugar.distancia)} km", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Button(onClick = onAbrirMapa, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Icon(Icons.Default.Directions, null, Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Ir")
            }
        }
    }
}

//AgregarContactoDialog - Diálogo para agregar un nuevo contacto de emergencia
//Pide nombre, teléfono y relación, y devuelve un ContactoEmergencia al confirmar
@Composable
fun AgregarContactoDialog(onDismiss: () -> Unit, onConfirm: (ContactoEmergencia) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var relacion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Contacto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre completo") }, placeholder = { Text("Ej: Dr. Juan Pérez") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = telefono, onValueChange = { if (it.all { char -> char.isDigit() || char == '+' }) telefono = it }, label = { Text("Teléfono") }, placeholder = { Text("Ej: +50212345678") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = relacion, onValueChange = { relacion = it }, label = { Text("Relación") }, placeholder = { Text("Ej: Familiar, Médico") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nombre.isNotBlank() && telefono.isNotBlank() && relacion.isNotBlank()) {
                    onConfirm(ContactoEmergencia((1..10000).random(), nombre, telefono, relacion))
                }
            }) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

//BotonAccion - Botón redondeado con icono y texto
//Se usa para acciones principales de la pantalla (enviar alerta, agregar contacto)
@Composable
private fun BotonAccion(texto: String, icono: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = c.secondaryContainer, contentColor = c.onSecondaryContainer),
        modifier = modifier.height(56.dp)
    ) {
        Icon(icono, null, Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(texto, fontSize = 14.sp)
    }
}

//ItemContacto - Tarjeta para mostrar un contacto de emergencia
//Muestra nombre, relación y botones para llamar o ver detalle
@Composable
private fun ItemContacto(contacto: ContactoEmergencia, onClick: () -> Unit, onLlamar: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(contacto.nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Text(contacto.relacion, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onLlamar, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("Llamar") }
                OutlinedButton(onClick = onClick) { Text("Ver") }
            }
        }
    }
}