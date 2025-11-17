//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto_1.ui.componentes.BarraInferior

//GuiaPrimerosAuxilios - Modelo de datos para cada guía de primeros auxilios
//Almacena el título que se muestra en la lista y el nombre del archivo PDF asociado
data class GuiaPrimerosAuxilios(
    val titulo: String,
    val archivoPdf: String
)

@OptIn(ExperimentalMaterial3Api::class)
//PantallaPrimerosAuxilios - Pantalla de guías de primeros auxilios
//Muestra una lista de guías y permite abrir cada PDF, con barra superior y (opcional) barra inferior
@Composable
fun PantallaPrimerosAuxilios(
    navController: NavController,          // Navegador para controlar rutas de la app
    onVolver: () -> Unit,                  // Acción para regresar a la pantalla anterior
    onAbrirGuia: (String) -> Unit,         // Acción para abrir una guía recibiendo el nombre del PDF
    mostrarBarraInferior: Boolean = false, // Indica si se muestra la barra de navegación inferior
    viewModel: PrimerosAuxiliosViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route
    val uiState by viewModel.uiState.collectAsState()

    // Lista completa de guías con sus archivos PDF correspondientes
    //Cada elemento representa un tema de primeros auxilios y el archivo que se abrirá al seleccionarlo
    val guias = listOf(
        GuiaPrimerosAuxilios("Fracturas", "fracturas.pdf"),
        GuiaPrimerosAuxilios("Quemaduras", "quemaduras.pdf"),
        GuiaPrimerosAuxilios("Desmayos", "desmayo.pdf"),
        GuiaPrimerosAuxilios("Atragantamiento", "guia_atragantamiento_husl.pdf"),
        GuiaPrimerosAuxilios("Hemorragias (sangrado abundante)", "hemorragias_shocks.pdf"),
        GuiaPrimerosAuxilios("Cortes y heridas leves", "heridas.pdf"),
        GuiaPrimerosAuxilios("Picaduras o mordeduras de animales", "picaduras_mordeduras.pdf"),
        GuiaPrimerosAuxilios("Golpe de calor (insolación)", "golpe_calor.pdf"),
        GuiaPrimerosAuxilios("Hipotermia", "guia_hipotermia_husl.pdf"),
        GuiaPrimerosAuxilios("Crisis epiléptica (convulsiones)", "guia_actuacion_epilepsia.pdf"),
        GuiaPrimerosAuxilios("Ataque cardíaco (infarto agudo)", "guia_ataque_cardiaco_husl.pdf"),
        GuiaPrimerosAuxilios("Dificultad respiratoria o asma", "guia_asma.pdf"),
        GuiaPrimerosAuxilios("Intoxicación o envenenamiento", "guia_intoxicaciones_envenenamiento_husl.pdf"),
        GuiaPrimerosAuxilios("Shock (colapso circulatorio)", "hemorragias_shocks.pdf"),
        GuiaPrimerosAuxilios("Traumatismo craneal (golpe en la cabeza)", "guia_traumatismo_craneal.pdf"),
        GuiaPrimerosAuxilios("Reanimación cardiopulmonar (RCP)", "rcp.pdf")
    )

    Scaffold(
        //Barra superior con título y botón de regreso
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Guías de primeros auxilios",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
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
        //Barra de navegación inferior opcional para mantener la navegación global de la app
        bottomBar = {
            if (mostrarBarraInferior) {
                BarraInferior(
                    navController = navController,
                    selectedRoute = selectedRoute
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                // Pantalla de carga mientras el ViewModel indica que se están preparando los datos
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 6.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            } else {
                // Contenido principal - Lista de guías de primeros auxilios
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //Por cada guía se crea un botón que permite abrir el PDF correspondiente
                    items(guias) { guia ->
                        BotonGuia(
                            texto = guia.titulo,
                            onClick = { onAbrirGuia(guia.archivoPdf) }
                        )
                    }

                    // Espaciado al final de la lista
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

//BotonGuia - Componente de tarjeta para cada guía
//Muestra el título de la guía y un ícono de flecha para indicar navegación al detalle/PDF
@Composable
private fun BotonGuia(texto: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },   //Al tocar la tarjeta se ejecuta la acción de abrir guía
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Texto con el nombre de la guía
            Text(
                text = texto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            //Ícono de flecha que refuerza la idea de navegación/avance
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir guía",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}