package com.example.proyecto_1.ui.feature.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import java.util.*

data class MedicalReminder(
    val id: Int, val title: String, val time: String, val day: Int, val month: Int, val year: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCalendar() {
    val calendar = Calendar.getInstance()
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    val reminders = remember {
        listOf(
            MedicalReminder(1, "Medicina 1", "8:00 a.m.", 17, 9, 2025),
            MedicalReminder(2, "Medicina 2", "10:00 a.m.", 17, 9, 2025),
            MedicalReminder(3, "Cita 1", "5:00 p.m.", 18, 9, 2025)
        )
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("9-41", fontSize = 18.sp, fontWeight = FontWeight.Bold) }) }
    ) { paddingValues ->
        Column(Modifier.fillMaxSize().padding(paddingValues).background(Color.White)) {
            LazyColumn(Modifier.weight(1f).padding(16.dp)) {
                items(3) { index ->
                    if (index < reminders.size) {
                        MedicalReminderCard(reminders[index]); Spacer(Modifier.height(8.dp))
                    }
                }
                item {
                    Spacer(Modifier.height(24.dp))
                    Text("Septiembre 2025", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                    CalendarView(selectedDay, { selectedDay = it }, reminders)
                    Spacer(Modifier.height(24.dp))
                    reminders.filter { it.day == selectedDay }.forEach {
                        MedicalReminderCard(it); Spacer(Modifier.height(8.dp))
                    }
                }
            }
            Button(
                onClick = { }, modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) { Icon(Icons.Default.Add, contentDescription = null); Spacer(Modifier.width(8.dp)); Text("Agregar Nuevo Recordatorio") }
        }
    }
}

@Composable
fun MedicalReminderCard(reminder: MedicalReminder) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(reminder.title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Text(reminder.time, fontSize = 14.sp, color = Color.Gray)
            }
            Icon(Icons.Default.MedicalServices, contentDescription = null, tint = Color(0xFF2196F3))
        }
    }
}

@Composable
fun CalendarView(selectedDay: Int, onDaySelected: (Int) -> Unit, reminders: List<MedicalReminder>) {
    val daysInMonth = 30
    val firstDayOfWeek = 1
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach {
                Text(it, modifier = Modifier.weight(1f).padding(4.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            }
        }
        for (week in 0 until 6) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (dayOfWeek in 0 until 7) {
                    val dayNumber = week * 7 + dayOfWeek - firstDayOfWeek + 2
                    if (dayNumber in 1..daysInMonth) {
                        val currentDay = dayNumber
                        val hasReminders = reminders.any { it.day == currentDay && it.month == 9 && it.year == 2025 }
                        Box(
                            modifier = Modifier.weight(1f).padding(4.dp).aspectRatio(1f)
                                .background(
                                    color = if (currentDay == selectedDay) Color(0xFF2196F3) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ).clickable { onDaySelected(currentDay) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = currentDay.toString(),
                                    fontSize = 14.sp,
                                    color = if (currentDay == selectedDay) Color.White else Color.Black,
                                    textAlign = TextAlign.Center
                                )
                                if (hasReminders) {
                                    Spacer(Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier.size(4.dp).background(
                                            color = if (currentDay == selectedDay) Color.White else Color(0xFF2196F3),
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        Box(Modifier.weight(1f).padding(4.dp).aspectRatio(1f))
                    }
                }
            }
        }
    }
}
