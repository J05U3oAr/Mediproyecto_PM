//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.calendario

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.RecordatorioMedico
import com.example.proyecto_1.notifications.NotificationScheduler
import java.util.*

//PantallaCalendar - Pantalla de gestión de recordatorios médicos
//PROPÓSITO:
//Permitir al usuario crear, visualizar y eliminar recordatorios para:
//Tomar medicamentos
//Asistir a citas médicas
//Cualquier evento médico importante
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCalendar(
    onVolver: () -> Unit = {},
    viewModel: CalendarioViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val uiState by viewModel.uiState.collectAsState()

    // Estados para el calendario
    var mesSeleccionado by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var anioSeleccionado by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    // Estados para los diálogos
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<RecordatorioMedico?>(null) }

    //Launcher para solicitar permiso de notificaciones en Android 13+
    //En Android 13 (API 33) y superior, las apps necesitan pedir
    //permiso explícito para mostrar notificaciones.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Los recordatorios necesitan permiso de notificaciones",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //LaunchedEffect - Solicita permiso de notificaciones al iniciar
    //Solo se ejecuta en Android 13+ (Tiramisu)
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Obtener recordatorios desde AppDataManager
    val recordatorios = AppDataManager.recordatorios

    // Nombres de los meses en español
    val meses = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Notificaciones",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = cs.secondaryContainer,
                    titleContentColor = cs.onSecondaryContainer,
                    navigationIconContentColor = cs.onSecondaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                //PANTALLA DE CARGA
                //Muestra spinner circular por 2 segundos
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

                //CONTENIDO PRINCIPAL
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(cs.background)
                ) {
                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        //SELECTOR DE MES Y AÑO
                        //Permite navegar entre meses con flechas
                        //Muestra el mes y año actual seleccionado
                        item {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Botón: Mes anterior
                                IconButton(onClick = {
                                    if (mesSeleccionado == 0) {
                                        mesSeleccionado = 11  // Diciembre
                                        anioSeleccionado--
                                    } else {
                                        mesSeleccionado--
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        "Mes anterior",
                                        tint = cs.onBackground
                                    )
                                }

                                // Texto: Mes y año
                                Text(
                                    "${meses[mesSeleccionado]} $anioSeleccionado",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = cs.onBackground
                                )

                                // Botón: Mes siguiente
                                IconButton(onClick = {
                                    if (mesSeleccionado == 11) {
                                        mesSeleccionado = 0  // Enero
                                        anioSeleccionado++
                                    } else {
                                        mesSeleccionado++
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.ArrowForward,
                                        "Mes siguiente",
                                        tint = cs.onBackground
                                    )
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }

                        //CALENDARIO VISUAL
                        //Componente que muestra:
                        //Días de la semana
                        //Días del mes en cuadrícula
                        //Día seleccionado resaltado
                        //Puntos indicadores en días con recordatorios
                        item {
                            CalendarView(
                                selectedDay = selectedDay,
                                onDaySelected = { selectedDay = it },
                                reminders = recordatorios.toList(),
                                mes = mesSeleccionado,
                                anio = anioSeleccionado
                            )
                            Spacer(Modifier.height(24.dp))
                        }

                        //SECCIÓN: RECORDATORIOS PRÓXIMOS
                        item {
                            Text(
                                "Recordatorios próximos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = cs.onBackground,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        //FILTRAR Y ORDENAR RECORDATORIOS
                        //1. Filtra solo recordatorios futuros (no pasados)
                        //2. Ordena por fecha (año, mes, día)
                        //3. Toma máximo 5 para mostrar
                        val upcomingReminders = recordatorios
                            .filter {
                                val fechaRecordatorio = Calendar.getInstance().apply {
                                    set(it.anio, it.mes - 1, it.dia)
                                }
                                !fechaRecordatorio.before(Calendar.getInstance())
                            }
                            .sortedWith(compareBy({ it.anio }, { it.mes }, { it.dia }))
                            .take(5)

                        //MOSTRAR RECORDATORIOS O MENSAJE VACÍO
                        if (upcomingReminders.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = cs.surfaceVariant
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            Icons.Default.EventNote,
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp),
                                            tint = cs.onSurfaceVariant
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "No hay recordatorios programados",
                                            fontSize = 16.sp,
                                            color = cs.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        } else {
                            items(upcomingReminders) { reminder ->
                                MedicalReminderCard(
                                    reminder = reminder,
                                    onDelete = { showDeleteDialog = reminder }
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        item {
                            Spacer(Modifier.height(24.dp))
                        }
                    }

                    //BOTÓN: AGREGAR NUEVO RECORDATORIO
                    //Botón fijo en la parte inferior
                    //Abre el diálogo para crear un recordatorio
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Agregar Nuevo Recordatorio")
                    }
                }
            }
        }
    }

    //DIÁLOGO: AGREGAR RECORDATORIO
    //Muestra formulario para crear nuevo recordatorio
    if (showDialog) {
        AgregarRecordatorioDialog(
            onDismiss = { showDialog = false },
            onConfirm = { nuevoRecordatorio ->
                // Agregar a AppDataManager
                AppDataManager.agregarRecordatorio(nuevoRecordatorio)

                // Programar notificación con WorkManager
                NotificationScheduler.programarNotificacion(context, nuevoRecordatorio)

                Toast.makeText(
                    context,
                    "Recordatorio programado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                showDialog = false
            }
        )
    }

    //DIÁLOGO: CONFIRMAR ELIMINACIÓN
    //Pide confirmación antes de eliminar un recordatorio
    showDeleteDialog?.let { recordatorio ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = cs.error
                )
            },
            title = { Text("Eliminar Recordatorio") },
            text = {
                Text("¿Estás seguro que deseas eliminar el recordatorio '${recordatorio.titulo}'?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Eliminar de AppDataManager
                        AppDataManager.eliminarRecordatorio(recordatorio.id)

                        // Cancelar notificación programada
                        NotificationScheduler.cancelarNotificacion(context, recordatorio.id)

                        Toast.makeText(
                            context,
                            "Recordatorio eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

//AgregarRecordatorioDialog - Diálogo para crear recordatorio
//CAMPOS:
//Nombre de la medicina o cita
//Fecha: día, mes, año
//Hora: hora, minuto, AM/PM
@Composable
fun AgregarRecordatorioDialog(
    onDismiss: () -> Unit,
    onConfirm: (RecordatorioMedico) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var nombreMedicina by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var minuto by remember { mutableStateOf("") }
    var esPM by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Agregar Recordatorio",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Campo: Nombre
                OutlinedTextField(
                    value = nombreMedicina,
                    onValueChange = { nombreMedicina = it },
                    label = { Text("Nombre de la medicina o cita") },
                    placeholder = { Text("Ej: Paracetamol") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = cs.onSurface,
                        unfocusedTextColor = cs.onSurface
                    )
                )

                // Sección: Fecha
                Text("Fecha:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = dia,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) dia = it },
                        label = { Text("Día") },
                        placeholder = { Text("15") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface
                        )
                    )
                    OutlinedTextField(
                        value = mes,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) mes = it },
                        label = { Text("Mes") },
                        placeholder = { Text("12") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface
                        )
                    )
                    OutlinedTextField(
                        value = anio,
                        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) anio = it },
                        label = { Text("Año") },
                        placeholder = { Text("2025") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface
                        )
                    )
                }

                // Sección: Hora
                Text("Hora:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = hora,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) hora = it },
                        label = { Text("Hora") },
                        placeholder = { Text("9") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface
                        )
                    )
                    Text(":", fontSize = 20.sp)
                    OutlinedTextField(
                        value = minuto,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) minuto = it },
                        label = { Text("Min") },
                        placeholder = { Text("00") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface
                        )
                    )

                    // Selector AM/PM
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !esPM,
                                onClick = { esPM = false }
                            )
                            Text("AM", fontSize = 14.sp)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = esPM,
                                onClick = { esPM = true }
                            )
                            Text("PM", fontSize = 14.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombreMedicina.isNotBlank() && dia.isNotBlank() &&
                        mes.isNotBlank() && anio.isNotBlank() &&
                        hora.isNotBlank() && minuto.isNotBlank()) {

                        val diaInt = dia.toIntOrNull() ?: 1
                        val mesInt = mes.toIntOrNull() ?: 1
                        val anioInt = anio.toIntOrNull() ?: 2025
                        val horaInt = hora.toIntOrNull() ?: 9
                        val minutoInt = minuto.toIntOrNull() ?: 0

                        val horaFormateada = "$horaInt:${minutoInt.toString().padStart(2, '0')} ${if (esPM) "p.m." else "a.m."}"

                        val nuevoId = System.currentTimeMillis().toInt()
                        val nuevoRecordatorio = RecordatorioMedico(
                            id = nuevoId,
                            titulo = nombreMedicina,
                            hora = horaFormateada,
                            dia = diaInt.coerceIn(1, 31),
                            mes = mesInt.coerceIn(1, 12),
                            anio = anioInt.coerceIn(2025, 2032)
                        )
                        onConfirm(nuevoRecordatorio)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary
                )
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

//MedicalReminderCard - Tarjeta de recordatorio médico
//Muestra:
//Icono de medicina
//Título del recordatorio
//Hora y fecha
//Botón de eliminar
@Composable
fun MedicalReminderCard(
    reminder: RecordatorioMedico,
    onDelete: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, cs.outline, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.MedicalServices,
                contentDescription = null,
                tint = cs.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    reminder.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = cs.onSurface
                )
                Text(
                    "${reminder.hora} - ${reminder.dia}/${reminder.mes}/${reminder.anio}",
                    fontSize = 14.sp,
                    color = cs.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = cs.error
                )
            }
        }
    }
}

//CalendarView - Componente de calendario mensual
//FUNCIONAMIENTO:
//1. Calcula cuántos días tiene el mes
//2. Determina en qué día de la semana empieza el mes
//3. Renderiza una cuadrícula de 6 semanas x 7 días
//4. Marca el día seleccionado con fondo azul
//5. Muestra puntos en días que tienen recordatorios

@Composable
fun CalendarView(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
    reminders: List<RecordatorioMedico>,
    mes: Int,
    anio: Int
) {
    val cs = MaterialTheme.colorScheme
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, anio)
        set(Calendar.MONTH, mes)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    // Calcular días del mes y primer día de la semana
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

    Column {
        // Encabezado de días de la semana
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("D", "L", "M", "X", "J", "V", "S").forEach {
                Text(
                    it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = cs.onSurfaceVariant
                )
            }
        }

        // Días del mes (6 semanas)
        for (week in 0 until 6) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 0 until 7) {
                    val dayNumber = week * 7 + dayOfWeek - firstDayOfWeek + 1

                    if (dayNumber in 1..daysInMonth) {
                        val currentDay = dayNumber
                        val hasReminders = reminders.any {
                            it.dia == currentDay && it.mes == mes + 1 && it.anio == anio
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f)
                                .background(
                                    color = if (currentDay == selectedDay)
                                        cs.primary
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onDaySelected(currentDay) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = currentDay.toString(),
                                    fontSize = 14.sp,
                                    color = if (currentDay == selectedDay)
                                        cs.onPrimary
                                    else
                                        cs.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                if (hasReminders) {
                                    Spacer(Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                color = if (currentDay == selectedDay)
                                                    cs.onPrimary
                                                else
                                                    cs.primary,
                                                shape = RoundedCornerShape(3.dp)
                                            )
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}