package com.example.proyecto_1.ui.feature.registro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.data.UsuarioRegistro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    sessionManager: SessionManager,
    onPerfilCompletado: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current

    // Obtener los datos actuales del usuario
    val usuarioActual = AppDataManager.usuarioRegistro.value

    // Si el usuario ya tiene nombre guardado, usarlo
    val nombreInicial = if (usuarioActual.nombre != "Invitado") {
        usuarioActual.nombre
    } else {
        sessionManager.getUserName()
    }

    // Estados locales para los campos
    var nombre by remember { mutableStateOf(nombreInicial) }
    var edad by remember { mutableStateOf(usuarioActual.edad) }
    var genero by remember { mutableStateOf(usuarioActual.genero) }
    var tipoSangre by remember { mutableStateOf(usuarioActual.tipoSangre) }
    var alergias by remember { mutableStateOf(usuarioActual.alergias) }
    var contactoEmergencia by remember { mutableStateOf(usuarioActual.contactoEmergencia) }

    // Menú desplegable para género
    var generoExpandido by remember { mutableStateOf(false) }
    val opcionesGenero = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")

    // Menú desplegable para tipo de sangre
    var tipoSangreExpandido by remember { mutableStateOf(false) }
    val opcionesTipoSangre = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        // Título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cs.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Registro Médico",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = cs.onSurface
            )
        }

        // Mensaje de bienvenida si es primera vez
        if (!sessionManager.isProfileCompleted()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cs.primaryContainer
                )
            ) {
                Text(
                    text = "¡Bienvenido/a! Por favor completa tu perfil médico.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        // Fila: Edad y Género
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Edad
            OutlinedTextField(
                value = edad,
                onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) edad = it },
                label = { Text("Edad") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Dropdown Género
            ExposedDropdownMenuBox(
                expanded = generoExpandido,
                onExpandedChange = { generoExpandido = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpandido) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                ExposedDropdownMenu(
                    expanded = generoExpandido,
                    onDismissRequest = { generoExpandido = false }
                ) {
                    opcionesGenero.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                genero = opcion
                                generoExpandido = false
                            }
                        )
                    }
                }
            }
        }

        // Tipo de sangre
        Text(
            "Tipo de sangre",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = cs.onSurface
        )

        ExposedDropdownMenuBox(
            expanded = tipoSangreExpandido,
            onExpandedChange = { tipoSangreExpandido = it }
        ) {
            OutlinedTextField(
                value = tipoSangre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar tipo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoSangreExpandido) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = tipoSangreExpandido,
                onDismissRequest = { tipoSangreExpandido = false }
            ) {
                opcionesTipoSangre.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            tipoSangre = opcion
                            tipoSangreExpandido = false
                        }
                    )
                }
            }
        }

        // Alergias
        Text(
            "Alergias",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = cs.onSurface
        )

        OutlinedTextField(
            value = alergias,
            onValueChange = { alergias = it },
            placeholder = { Text("Ej: Penicilina, polen, mariscos") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp),
            shape = RoundedCornerShape(10.dp),
            maxLines = 3
        )

        // Contacto de emergencia
        Text(
            "Contacto de emergencia",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = cs.onSurface
        )

        OutlinedTextField(
            value = contactoEmergencia,
            onValueChange = { contactoEmergencia = it },
            placeholder = { Text("Nombre y teléfono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )

        // ✅ NUEVO: Botón para regresar al menú principal (solo si el perfil ya está completado)
        if (sessionManager.isProfileCompleted()) {
            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    onPerfilCompletado()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = cs.primary
                )
            ) {
                Text(
                    "Regresar al menú principal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Botón Guardar
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // Validar que al menos el nombre esté lleno
                    if (nombre.isBlank()) {
                        Toast.makeText(
                            context,
                            "Por favor ingresa tu nombre",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Guardar los datos
                        val nuevoUsuario = UsuarioRegistro(
                            nombre = nombre,
                            edad = edad,
                            genero = genero,
                            tipoSangre = tipoSangre,
                            alergias = alergias,
                            contactoEmergencia = contactoEmergencia
                        )
                        AppDataManager.actualizarUsuario(nuevoUsuario)

                        val mensaje = if (!sessionManager.isProfileCompleted()) {
                            "✓ Perfil completado. ¡Bienvenido/a!"
                        } else {
                            "✓ Perfil actualizado correctamente"
                        }

                        Toast.makeText(
                            context,
                            mensaje,
                            Toast.LENGTH_LONG
                        ).show()

                        // Siempre navegar a Inicio después de guardar
                        onPerfilCompletado()
                    }
                },
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.secondaryContainer,
                    contentColor = cs.onSecondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(64.dp)
            ) {
                Text(
                    if (!sessionManager.isProfileCompleted()) "Continuar" else "Guardar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}