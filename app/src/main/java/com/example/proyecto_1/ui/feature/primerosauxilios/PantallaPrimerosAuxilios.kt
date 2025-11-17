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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto_1.ui.componentes.BarraInferior

data class GuiaPrimerosAuxilios(
    val titulo: String,
    val archivoPdf: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrimerosAuxilios(
    navController: NavController,
    onVolver: () -> Unit,
    onAbrirGuia: (String) -> Unit,
    mostrarBarraInferior: Boolean = false,
    viewModel: PrimerosAuxiliosViewModel = viewModel()
) {
    val cs = MaterialTheme.colorScheme
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route
    val uiState by viewModel.uiState.collectAsState()

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
                    containerColor = cs.secondaryContainer,
                    titleContentColor = cs.onSecondaryContainer,
                    navigationIconContentColor = cs.onSecondaryContainer
                )
            )
        },
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(cs.background)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(guias) { guia ->
                        BotonGuia(
                            texto = guia.titulo,
                            onClick = { onAbrirGuia(guia.archivoPdf) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BotonGuia(texto: String, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant
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
            Text(
                text = texto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = cs.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir guía",
                tint = cs.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}