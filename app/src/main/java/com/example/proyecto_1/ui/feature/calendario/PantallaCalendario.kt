package com.example.proyecto_1.ui.feature.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.RecordatorioMedico
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCalendar(onVolver: () -> Unit = {}) {
    val calendar = Calendar.getInstance()
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var showDialog by remember { mutableStateOf(false) }

    // Usar los recordatorios del gestor global
    val recordatorios = AppDataManager.recordatorios

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
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Mostrar los primeros 3 recordatorios más próximos
                val upcomingReminders = recordatorios.sortedWith(
                    compareBy({ it.anio }, { it.mes }, { it.dia })
                ).take(3)

                items(upcomingReminders) { reminder ->
                    MedicalReminderCard(reminder)
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Septiembre 2025",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    CalendarView(selectedDay, { selectedDay = it }, recordatorios.toList())
                    Spacer(Modifier.height(24.dp))

                    // Mostrar recordatorios del día seleccionado
                    val selectedDayReminders = recordatorios.filter {
                        it.dia == selectedDay && it.mes == 9 && it.anio == 2025
                    }

                    if (selectedDayReminders.isNotEmpty()) {
                        Text(
                            "Recordatorios para el día $selectedDay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        selectedDayReminders.forEach {
                            MedicalReminderCard(it)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Agregar Nuevo Recordatorio")
            }
        }
    }

    // Diálogo para agregar recordatorio
    if (showDialog) {
        AgregarRecordatorioDialog(
            onDismiss = { showDialog = false },
            onConfirm = { nuevoRecordatorio ->
                AppDataManager.agregarRecordatorio(nuevoRecordatorio)
                showDialog = false
            }
        )
    }
}

@Composable
fun AgregarRecordatorioDialog(
    onDismiss: () -> Unit,
    onConfirm: (RecordatorioMedico) -> Unit
) {
    var nombreMedicina by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

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
                OutlinedTextField(
                    value = nombreMedicina,
                    onValueChange = { nombreMedicina = it },
                    label = { Text("Nombre de la medicina") },
                    placeholder = { Text("Ej: Paracetamol") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = dia,
                    onValueChange = { if (it.length <= 2) dia = it },
                    label = { Text("Día (1-30)") },
                    placeholder = { Text("Ej: 15") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora") },
                    placeholder = { Text("Ej: 9:00 a.m.") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombreMedicina.isNotBlank() && dia.isNotBlank() && hora.isNotBlank()) {
                        val diaInt = dia.toIntOrNull() ?: 1
                        val nuevoId = (1..10000).random()
                        val nuevoRecordatorio = RecordatorioMedico(
                            id = nuevoId,
                            titulo = nombreMedicina,
                            hora = hora,
                            dia = diaInt.coerceIn(1, 30),
                            mes = 9,
                            anio = 2025
                        )
                        onConfirm(nuevoRecordatorio)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
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

@Composable
fun MedicalReminderCard(reminder: RecordatorioMedico) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    reminder.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    "${reminder.hora} - ${reminder.dia}/${reminder.mes}/${reminder.anio}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Icon(
                Icons.Default.MedicalServices,
                contentDescription = null,
                tint = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun CalendarView(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
    reminders: List<RecordatorioMedico>
) {
    val daysInMonth = 30
    val firstDayOfWeek = 1

    Column {
        // Encabezado de días de la semana
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach {
                Text(
                    it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Días del mes
        for (week in 0 until 6) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 0 until 7) {
                    val dayNumber = week * 7 + dayOfWeek - firstDayOfWeek + 2

                    if (dayNumber in 1..daysInMonth) {
                        val currentDay = dayNumber
                        val hasReminders = reminders.any {
                            it.dia == currentDay && it.mes == 9 && it.anio == 2025
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f)
                                .background(
                                    color = if (currentDay == selectedDay)
                                        Color(0xFF2196F3)
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
                                        Color.White
                                    else
                                        Color.Black,
                                    textAlign = TextAlign.Center
                                )
                                if (hasReminders) {
                                    Spacer(Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                color = if (currentDay == selectedDay)
                                                    Color.White
                                                else
                                                    Color(0xFF2196F3),
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