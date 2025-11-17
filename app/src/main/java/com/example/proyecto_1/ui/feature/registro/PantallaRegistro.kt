//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.registro

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.data.UsuarioRegistro
import com.example.proyecto_1.data.database.AppDatabase
import com.example.proyecto_1.data.database.PerfilMedico
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
//PantallaRegistro
//Permite guardar datos básicos, tipo de sangre, alergias y contacto de emergencia
@Composable
fun PantallaRegistro(
    sessionManager: SessionManager,
    onPerfilCompletado: () -> Unit = {},
    viewModel: RegistroViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    val database = remember { AppDatabase.getInstance(context) }
    val perfilDao = database.perfilMedicoDao()
    val userEmail = sessionManager.getUserEmail()

    LaunchedEffect(userEmail) {
        if (userEmail.isNotBlank()) {
            val perfilExistente = perfilDao.obtenerPerfilPorEmail(userEmail)
            if (perfilExistente != null) {
                AppDataManager.actualizarUsuario(
                    UsuarioRegistro(
                        nombre = perfilExistente.nombre,
                        edad = perfilExistente.edad,
                        genero = perfilExistente.genero,
                        tipoSangre = perfilExistente.tipoSangre,
                        alergias = perfilExistente.alergias,
                        contactoEmergenciaNombre = perfilExistente.contactoEmergenciaNombre,
                        contactoEmergenciaNumero = perfilExistente.contactoEmergenciaNumero
                    )
                )
            }
        }
    }

    val usuarioActual = AppDataManager.usuarioRegistro.value
    val nombreInicial = if (usuarioActual.nombre != "Invitado") {
        usuarioActual.nombre
    } else {
        sessionManager.getUserName()
    }

    var nombre by remember { mutableStateOf(nombreInicial) }
    var edad by remember { mutableStateOf(usuarioActual.edad) }
    var genero by remember { mutableStateOf(usuarioActual.genero) }
    var tipoSangre by remember { mutableStateOf(usuarioActual.tipoSangre) }
    var alergias by remember { mutableStateOf(usuarioActual.alergias) }
    var contactoEmergenciaNombre by remember { mutableStateOf(usuarioActual.contactoEmergenciaNombre) }
    var contactoEmergenciaNumero by remember { mutableStateOf(usuarioActual.contactoEmergenciaNumero) }
    var guardando by remember { mutableStateOf(false) }

    LaunchedEffect(usuarioActual) {
        if (usuarioActual.nombre != "Invitado") {
            nombre = usuarioActual.nombre
            edad = usuarioActual.edad
            genero = usuarioActual.genero
            tipoSangre = usuarioActual.tipoSangre
            alergias = usuarioActual.alergias
            contactoEmergenciaNombre = usuarioActual.contactoEmergenciaNombre
            contactoEmergenciaNumero = usuarioActual.contactoEmergenciaNumero
        }
    }

    var generoExpandido by remember { mutableStateOf(false) }
    val opcionesGenero = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")

    var tipoSangreExpandido by remember { mutableStateOf(false) }
    val opcionesTipoSangre = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    val contactPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permiso de contactos denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val pickContactLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val cursor = context.contentResolver.query(uri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)

                            if (nameIndex >= 0) {
                                contactoEmergenciaNombre = it.getString(nameIndex) ?: ""
                            }

                            if (idIndex >= 0) {
                                val contactId = it.getString(idIndex)
                                val phoneCursor = context.contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    arrayOf(contactId),
                                    null
                                )
                                phoneCursor?.use { pc ->
                                    if (pc.moveToFirst()) {
                                        val phoneIndex = pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                        if (phoneIndex >= 0) {
                                            contactoEmergenciaNumero = pc.getString(phoneIndex)?.replace("[^0-9+]".toRegex(), "") ?: ""
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al obtener contacto: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cs.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(Modifier.height(10.dp))

                // Encabezado
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
                        color = cs.onSecondaryContainer
                    )
                }

                if (!sessionManager.hasMedicalProfile()) {
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
                            fontWeight = FontWeight.Medium,
                            color = cs.onPrimaryContainer
                        )
                    }
                }

                // Campo: Nombre completo
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    enabled = !guardando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = cs.onSurface,
                        unfocusedTextColor = cs.onSurface,
                        disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                        focusedLabelColor = cs.primary,
                        unfocusedLabelColor = cs.onSurfaceVariant
                    )
                )

                // Fila con Edad y Género
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = edad,
                        onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) edad = it },
                        label = { Text("Edad") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = !guardando,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface,
                            disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                            focusedLabelColor = cs.primary,
                            unfocusedLabelColor = cs.onSurfaceVariant
                        )
                    )

                    ExposedDropdownMenuBox(
                        expanded = generoExpandido,
                        onExpandedChange = { generoExpandido = it && !guardando },
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
                            shape = RoundedCornerShape(10.dp),
                            enabled = !guardando,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = cs.onSurface,
                                unfocusedTextColor = cs.onSurface,
                                disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                                focusedLabelColor = cs.primary,
                                unfocusedLabelColor = cs.onSurfaceVariant
                            )
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

                // Sección: Tipo de sangre
                Text(
                    "Tipo de sangre",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = cs.onSurface
                )

                ExposedDropdownMenuBox(
                    expanded = tipoSangreExpandido,
                    onExpandedChange = { tipoSangreExpandido = it && !guardando }
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
                        shape = RoundedCornerShape(10.dp),
                        enabled = !guardando,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface,
                            disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                            focusedLabelColor = cs.primary,
                            unfocusedLabelColor = cs.onSurfaceVariant
                        )
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

                // Sección: Alergias
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
                    maxLines = 3,
                    enabled = !guardando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = cs.onSurface,
                        unfocusedTextColor = cs.onSurface,
                        disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                        focusedLabelColor = cs.primary,
                        unfocusedLabelColor = cs.onSurfaceVariant,
                        focusedPlaceholderColor = cs.onSurfaceVariant,
                        unfocusedPlaceholderColor = cs.onSurfaceVariant
                    )
                )

                // Sección: Contacto de emergencia
                Text(
                    "Contacto de emergencia",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = cs.onSurface
                )

                OutlinedTextField(
                    value = contactoEmergenciaNombre,
                    onValueChange = { contactoEmergenciaNombre = it },
                    label = { Text("Nombre del contacto") },
                    placeholder = { Text("Ej: Juan Pérez") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    enabled = !guardando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = cs.onSurface,
                        unfocusedTextColor = cs.onSurface,
                        disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                        focusedLabelColor = cs.primary,
                        unfocusedLabelColor = cs.onSurfaceVariant,
                        focusedPlaceholderColor = cs.onSurfaceVariant,
                        unfocusedPlaceholderColor = cs.onSurfaceVariant
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = contactoEmergenciaNumero,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() || char == '+' }) {
                                contactoEmergenciaNumero = it
                            }
                        },
                        label = { Text("Número de teléfono") },
                        placeholder = { Text("Ej: +50212345678") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        enabled = !guardando,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cs.onSurface,
                            unfocusedTextColor = cs.onSurface,
                            disabledTextColor = cs.onSurface.copy(alpha = 0.38f),
                            focusedLabelColor = cs.primary,
                            unfocusedLabelColor = cs.onSurfaceVariant,
                            focusedPlaceholderColor = cs.onSurfaceVariant,
                            unfocusedPlaceholderColor = cs.onSurfaceVariant
                        )
                    )

                    IconButton(
                        onClick = {
                            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                            pickContactLauncher.launch(intent)
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .padding(top = 8.dp),
                        enabled = !guardando
                    ) {
                        Icon(
                            imageVector = Icons.Default.Contacts,
                            contentDescription = "Seleccionar contacto",
                            tint = if (guardando) cs.onSurface.copy(alpha = 0.38f) else cs.primary
                        )
                    }
                }

                if (sessionManager.hasMedicalProfile()) {
                    Spacer(Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { onPerfilCompletado() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = cs.primary
                        ),
                        enabled = !guardando
                    ) {
                        Text(
                            "Regresar al menú principal",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            when {
                                nombre.isBlank() -> {
                                    Toast.makeText(context, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show()
                                }
                                contactoEmergenciaNombre.isBlank() -> {
                                    Toast.makeText(context, "Por favor ingresa el nombre del contacto de emergencia", Toast.LENGTH_SHORT).show()
                                }
                                contactoEmergenciaNumero.isBlank() -> {
                                    Toast.makeText(context, "Por favor ingresa el número del contacto de emergencia", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    guardando = true

                                    val nuevoUsuario = UsuarioRegistro(
                                        nombre = nombre,
                                        edad = edad,
                                        genero = genero,
                                        tipoSangre = tipoSangre,
                                        alergias = alergias,
                                        contactoEmergenciaNombre = contactoEmergenciaNombre,
                                        contactoEmergenciaNumero = contactoEmergenciaNumero
                                    )
                                    AppDataManager.actualizarUsuario(nuevoUsuario)

                                    scope.launch {
                                        try {
                                            val perfilMedico = PerfilMedico(
                                                emailUsuario = userEmail,
                                                nombre = nombre,
                                                edad = edad,
                                                genero = genero,
                                                tipoSangre = tipoSangre,
                                                alergias = alergias,
                                                contactoEmergenciaNombre = contactoEmergenciaNombre,
                                                contactoEmergenciaNumero = contactoEmergenciaNumero
                                            )

                                            perfilDao.insertarPerfil(perfilMedico)
                                            sessionManager.markProfileCompleted()

                                            val mensaje = if (!sessionManager.hasMedicalProfile()) {
                                                "✓ Perfil completado. ¡Bienvenido/a!"
                                            } else {
                                                "✓ Perfil actualizado correctamente"
                                            }

                                            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                                            onPerfilCompletado()
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                                            guardando = false
                                        }
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.secondaryContainer,
                            contentColor = cs.onSecondaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(64.dp),
                        enabled = !guardando
                    ) {
                        if (guardando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = cs.onSecondaryContainer
                            )
                        } else {
                            Text(
                                if (!sessionManager.hasMedicalProfile()) "Continuar" else "Guardar",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}