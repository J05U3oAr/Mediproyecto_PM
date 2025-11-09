package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    mostrarBarraInferior: Boolean = true
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route

    // Lista completa de guías con sus archivos PDF correspondientes
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
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(guias) { guia ->
                BotonGuia(
                    texto = guia.titulo,
                    onClick = { onAbrirGuia(guia.archivoPdf) }
                )
            }

            // Espaciado al final
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BotonGuia(texto: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
            Text(
                text = texto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir guía",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}