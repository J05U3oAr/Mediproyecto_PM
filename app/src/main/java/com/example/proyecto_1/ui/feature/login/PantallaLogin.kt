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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendary.R

@Composable
fun PantallaAuth(
    onContinuar: (() -> Unit)? = null
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
                    onToggle = { esLogin = false },
                    onContinuar = onContinuar
                )
            } else {
                FormularioRegistro(
                    onToggle = { esLogin = true },
                    onContinuar = onContinuar
                )
            }
        }
    }
}

@Composable
fun FormularioLogin(
    onToggle: () -> Unit,
    onContinuar: (() -> Unit)? = null
) {
    val contexto = androidx.compose.ui.platform.LocalContext.current
    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }

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
            text = "Introduce tu correo y contraseña",
            fontSize = 14.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            placeholder = { Text("email@domain.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contra,
            onValueChange = { contra = it },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // OPCIÓN A: entrar sin validar nada
        Button(
            onClick = {
                onContinuar?.invoke()
                // Si no pasas navegación, muestra un aviso y continúa
                if (onContinuar == null) {
                    Toast.makeText(contexto, "Entrando…", Toast.LENGTH_SHORT).show()
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
            Text("Continuar")
        }

        /*
        Button(
            onClick = {
                if (correo.isNotEmpty() &&
                    Patterns.EMAIL_ADDRESS.matcher(correo).matches() &&
                    contra.isNotEmpty()
                ) {
                    onContinuar?.invoke()
                    if (onContinuar == null) {
                        Toast.makeText(contexto, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(contexto, "Datos inválidos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) { Text("Continuar") }
        */


        TextButton(onClick = onToggle) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

@Composable
fun FormularioRegistro(
    onToggle: () -> Unit,
    onContinuar: (() -> Unit)? = null
) {
    val contexto = androidx.compose.ui.platform.LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contra by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

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
            text = "Introduce tu información para registrarte en la app",
            fontSize = 14.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            placeholder = { Text("email@domain.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contra,
            onValueChange = { contra = it },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onContinuar?.invoke()
                if (onContinuar == null) {
                    Toast.makeText(contexto, "Entrando…", Toast.LENGTH_SHORT).show()
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
