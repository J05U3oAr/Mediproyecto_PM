//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.calendary.R
import com.example.proyecto_1.data.AppDataManager
import com.example.proyecto_1.data.SessionManager
import com.example.proyecto_1.data.UsuarioRegistro
import com.example.proyecto_1.data.database.AppDatabase
import com.example.proyecto_1.ui.componentes.BarraInferior
import kotlinx.coroutines.launch

// Composable principal que muestra el perfil del usuario con su información médica
// Carga datos desde Room Database y los sincroniza con AppDataManager
// Permite cerrar sesión con confirmación
//
// Parámetros:
//   - sessionManager: Gestor de sesión para obtener email y cerrar sesión
//   - onCerrarSesion: Callback ejecutado al confirmar cierre de sesión
//   - navController: Controlador de navegación para la barra inferior
//   - modifier: Modificadores opcionales para personalización
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    sessionManager: SessionManager,
    onCerrarSesion: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Inicializa la base de datos Room y obtiene el DAO
    val database = remember { AppDatabase.getInstance(context) }
    val perfilDao = database.perfilMedicoDao()

    // Obtiene el email del usuario autenticado
    val userEmail = sessionManager.getUserEmail()

    // Efecto que se ejecuta al iniciar o cuando cambia el email
    // Carga el perfil médico desde la base de datos
    LaunchedEffect(userEmail) {
        if (userEmail.isNotBlank()) {
            // Obtiene el perfil del usuario desde Room
            val perfilExistente = perfilDao.obtenerPerfilPorEmail(userEmail)
            if (perfilExistente != null) {
                // Actualiza AppDataManager con los datos del perfil
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

    // Obtiene los datos del usuario desde el gestor global
    val usuario = AppDataManager.usuarioRegistro.value

    // Estado para controlar el diálogo de confirmación de cierre de sesión
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    // Observa la ruta actual para la barra inferior
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // Scaffold proporciona la estructura básica con topBar y bottomBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        bottomBar = {
            BarraInferior(
                navController = navController,
                selectedRoute = rutaActual
            )
        }
    ) { padding ->
        // Columna principal con scroll vertical
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Imagen de perfil circular
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            // Card que muestra el correo del usuario autenticado
            if (userEmail.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Cuenta",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            userEmail,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Columna con toda la información médica del usuario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título de la sección
                Text(
                    "Información Médica",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                // Card con nombre del usuario
                InfoCard(
                    titulo = "Nombre",
                    valor = if (usuario.nombre.isNotBlank()) usuario.nombre else "No especificado"
                )

                // Card con edad (solo si está especificada)
                if (usuario.edad.isNotBlank()) {
                    InfoCard(
                        titulo = "Edad",
                        valor = "${usuario.edad} años"
                    )
                }

                // Card con género (solo si está especificado)
                if (usuario.genero.isNotBlank()) {
                    InfoCard(
                        titulo = "Género",
                        valor = usuario.genero
                    )
                }

                // Card con tipo de sangre (solo si está especificado)
                if (usuario.tipoSangre.isNotBlank()) {
                    InfoCard(
                        titulo = "Tipo de sangre",
                        valor = usuario.tipoSangre
                    )
                }

                // Card con alergias (solo si están especificadas)
                if (usuario.alergias.isNotBlank()) {
                    InfoCard(
                        titulo = "Alergias",
                        valor = usuario.alergias
                    )
                }

                // Sección de contacto de emergencia (si existe)
                if (usuario.contactoEmergenciaNombre.isNotBlank() || usuario.contactoEmergenciaNumero.isNotBlank()) {
                    Text(
                        "Contacto de Emergencia",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Card con nombre del contacto
                    if (usuario.contactoEmergenciaNombre.isNotBlank()) {
                        InfoCard(
                            titulo = "Nombre",
                            valor = usuario.contactoEmergenciaNombre
                        )
                    }

                    // Card con teléfono del contacto
                    if (usuario.contactoEmergenciaNumero.isNotBlank()) {
                        InfoCard(
                            titulo = "Teléfono",
                            valor = usuario.contactoEmergenciaNumero
                        )
                    }
                }

                // Separador visual
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Información del equipo de desarrollo
                Text(
                    "Equipo de desarrollo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Carnés: Luis Hernández (241424), Sebastian Lemus (241155), Arodi Chavez (241112)",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))

            // Botón de cerrar sesión en rojo
            Button(
                onClick = { mostrarDialogoCerrarSesion = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    // Diálogo de confirmación para cerrar sesión
    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            title = {
                Text(
                    "Cerrar sesión",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro que deseas cerrar sesión? Tu información médica estará guardada cuando vuelvas a iniciar sesión.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Resetea todos los datos del AppDataManager
                        AppDataManager.resetearDatos()
                        // Ejecuta el callback de cierre de sesión
                        onCerrarSesion()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

//Composable privado que muestra una tarjeta con un par título-valor
//Se utiliza para mostrar cada campo de información del usuario
//Parámetros:
//titulo: Etiqueta del campo (ej: "Nombre", "Edad")
//valor: Contenido del campo
@Composable
private fun InfoCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título en gris y más pequeño
            Text(
                titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            // Valor en negro y más grande
            Text(
                valor,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}