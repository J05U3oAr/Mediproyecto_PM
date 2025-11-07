package com.example.proyecto_1.ui.feature.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendary.R
import com.example.proyecto_1.data.SessionManager

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

            Spacer(modifier = Modifier.height(8.dp))

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
    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

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
            isError = mostrarError
        )

        OutlinedTextField(
            value = contra,
            onValueChange = {
                contra = it
                mostrarError = false
            },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError
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
                    contra.length < 6 -> {
                        mostrarError = true
                        mensajeError = "La contraseña debe tener al menos 6 caracteres"
                    }
                    else -> {
                        // Login exitoso
                        val nombre = correo.substringBefore("@")
                        Toast.makeText(
                            contexto,
                            "¡Bienvenido/a!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onLoginExitoso(correo, nombre)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Iniciar sesión")
        }

        TextButton(onClick = onToggle) {
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
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

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
            isError = mostrarError && nombre.isBlank()
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
            isError = mostrarError
        )

        OutlinedTextField(
            value = contra,
            onValueChange = {
                contra = it
                mostrarError = false
            },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError
        )

        OutlinedTextField(
            value = confirmar,
            onValueChange = {
                confirmar = it
                mostrarError = false
            },
            label = { Text("Confirmar contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = mostrarError
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
                        // Registro exitoso
                        Toast.makeText(
                            contexto,
                            "¡Cuenta creada exitosamente!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onRegistroExitoso(correo, nombre)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onToggle) {
            Text("¿Ya tienes cuenta? Inicia sesión aquí")
        }
    }
}