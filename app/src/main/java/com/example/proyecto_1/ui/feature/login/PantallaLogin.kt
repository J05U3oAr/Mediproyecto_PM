package com.example.proyecto_1.ui.feature.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendary.R
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.data.database.AppDatabase
import com.example.proyecto_1.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

@Composable
fun PantallaAuth(
    sessionManager: SessionManager,
    onLoginExitoso: (email: String, nombre: String) -> Unit
) {
    var esLogin by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_login),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "HusL",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Help us Lifes",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (esLogin) {
                FormularioLogin(
                    sessionManager = sessionManager,
                    onToggle = { esLogin = false },
                    onLoginExitoso = onLoginExitoso
                )
            } else {
                FormularioRegistro(
                    sessionManager = sessionManager,
                    onToggle = { esLogin = true },
                    onRegistroExitoso = onLoginExitoso
                )
            }
        }
    }
}

@Composable
fun FormularioLogin(
    sessionManager: SessionManager,
    onToggle: () -> Unit,
    onLoginExitoso: (email: String, nombre: String) -> Unit
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    // Inicializar el repositorio
    val database = remember { AppDatabase.getInstance(contexto) }
    val repository = remember { UsuarioRepository(database.usuarioDao()) }

    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var mostrarPassword by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Inicia sesión",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Usa tu correo institucional UVG",
            fontSize = 14.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                mostrarError = false
            },
            label = { Text("Correo electrónico") },
            placeholder = { Text("ejemplo@uvg.edu.gt") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError,
            enabled = !cargando
        )

        OutlinedTextField(
            value = contra,
            onValueChange = {
                contra = it
                mostrarError = false
            },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                    Icon(
                        imageVector = if (mostrarPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (mostrarPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError,
            enabled = !cargando
        )

        if (mostrarError) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = {
                // Validación del correo
                when {
                    correo.isBlank() || contra.isBlank() -> {
                        mostrarError = true
                        mensajeError = "Por favor completa todos los campos"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                        mostrarError = true
                        mensajeError = "Correo electrónico inválido"
                    }
                    !correo.endsWith("@uvg.edu.gt") -> {
                        mostrarError = true
                        mensajeError = "Debes usar tu correo institucional @uvg.edu.gt"
                    }
                    else -> {
                        cargando = true
                        scope.launch {
                            val (exito, usuario) = repository.iniciarSesion(correo, contra)

                            if (exito && usuario != null) {
                                Toast.makeText(
                                    contexto,
                                    "¡Bienvenido/a ${usuario.nombre}!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onLoginExitoso(usuario.email, usuario.nombre)
                            } else {
                                mostrarError = true
                                mensajeError = "Credenciales incorrectas. Verifica tu correo y contraseña."
                                cargando = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("Iniciar sesión")
            }
        }

        TextButton(
            onClick = onToggle,
            enabled = !cargando
        ) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

@Composable
fun FormularioRegistro(
    sessionManager: SessionManager,
    onToggle: () -> Unit,
    onRegistroExitoso: (email: String, nombre: String) -> Unit
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    // Inicializar el repositorio
    val database = remember { AppDatabase.getInstance(contexto) }
    val repository = remember { UsuarioRepository(database.usuarioDao()) }

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var mostrarPassword by remember { mutableStateOf(false) }
    var mostrarConfirmar by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Crea tu cuenta",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "Usa tu correo institucional UVG",
            fontSize = 14.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mostrarError = false
            },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError && nombre.isBlank(),
            enabled = !cargando
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                mostrarError = false
            },
            label = { Text("Correo electrónico") },
            placeholder = { Text("ejemplo@uvg.edu.gt") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError,
            enabled = !cargando
        )

        OutlinedTextField(
            value = contra,
            onValueChange = {
                contra = it
                mostrarError = false
            },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                    Icon(
                        imageVector = if (mostrarPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (mostrarPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError,
            enabled = !cargando
        )

        OutlinedTextField(
            value = confirmar,
            onValueChange = {
                confirmar = it
                mostrarError = false
            },
            label = { Text("Confirmar contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (mostrarConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarConfirmar = !mostrarConfirmar }) {
                    Icon(
                        imageVector = if (mostrarConfirmar) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (mostrarConfirmar) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError,
            enabled = !cargando
        )

        if (mostrarError) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = {
                when {
                    nombre.isBlank() || correo.isBlank() || contra.isBlank() || confirmar.isBlank() -> {
                        mostrarError = true
                        mensajeError = "Por favor completa todos los campos"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                        mostrarError = true
                        mensajeError = "Correo electrónico inválido"
                    }
                    !correo.endsWith("@uvg.edu.gt") -> {
                        mostrarError = true
                        mensajeError = "Debes usar tu correo institucional @uvg.edu.gt"
                    }
                    contra.length < 6 -> {
                        mostrarError = true
                        mensajeError = "La contraseña debe tener al menos 6 caracteres"
                    }
                    contra != confirmar -> {
                        mostrarError = true
                        mensajeError = "Las contraseñas no coinciden"
                    }
                    else -> {
                        cargando = true
                        scope.launch {
                            val (exito, mensaje) = repository.registrarUsuario(correo, nombre, contra)

                            if (exito) {
                                Toast.makeText(
                                    contexto,
                                    "¡Cuenta creada exitosamente!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onRegistroExitoso(correo, nombre)
                            } else {
                                mostrarError = true
                                mensajeError = mensaje
                                cargando = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("Registrarse")
            }
        }

        TextButton(
            onClick = onToggle,
            enabled = !cargando
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión aquí")
        }
    }
}