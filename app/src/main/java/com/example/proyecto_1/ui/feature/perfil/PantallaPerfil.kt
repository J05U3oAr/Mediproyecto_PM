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

    // Base de datos
    val database = remember { AppDatabase.getInstance(context) }
    val perfilDao = database.perfilMedicoDao()

    val userEmail = sessionManager.getUserEmail()

    // Cargar perfil desde BD
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

    // Obtener los datos del usuario desde el gestor global
    val usuario = AppDataManager.usuarioRegistro.value

    // Estado para el diálogo de confirmación
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    // Obtener la ruta actual para la barra inferior
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

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
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            // Mostrar correo del usuario
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

            // Información del usuario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Información Médica",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                // Nombre
                InfoCard(
                    titulo = "Nombre",
                    valor = if (usuario.nombre.isNotBlank()) usuario.nombre else "No especificado"
                )

                // Edad
                if (usuario.edad.isNotBlank()) {
                    InfoCard(
                        titulo = "Edad",
                        valor = "${usuario.edad} años"
                    )
                }

                // Género
                if (usuario.genero.isNotBlank()) {
                    InfoCard(
                        titulo = "Género",
                        valor = usuario.genero
                    )
                }

                // Tipo de sangre
                if (usuario.tipoSangre.isNotBlank()) {
                    InfoCard(
                        titulo = "Tipo de sangre",
                        valor = usuario.tipoSangre
                    )
                }

                // Alergias
                if (usuario.alergias.isNotBlank()) {
                    InfoCard(
                        titulo = "Alergias",
                        valor = usuario.alergias
                    )
                }

                // Contacto de emergencia
                if (usuario.contactoEmergenciaNombre.isNotBlank() || usuario.contactoEmergenciaNumero.isNotBlank()) {
                    Text(
                        "Contacto de Emergencia",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (usuario.contactoEmergenciaNombre.isNotBlank()) {
                        InfoCard(
                            titulo = "Nombre",
                            valor = usuario.contactoEmergenciaNombre
                        )
                    }

                    if (usuario.contactoEmergenciaNumero.isNotBlank()) {
                        InfoCard(
                            titulo = "Teléfono",
                            valor = usuario.contactoEmergenciaNumero
                        )
                    }
                }

                // Información del equipo de desarrollo
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    "Equipo de desarrollo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Carnés: 241424, 241155, 241112",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))

            // Botón de cerrar sesión
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
                        AppDataManager.resetearDatos()
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
            Text(
                titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                valor,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}