package com.example.proyecto_1.ui.feature.primerosauxilios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto_1.ui.componentes.BarraInferior

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
                }
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            BotonGuia("Fracturas")       { onAbrirGuia("fracturas") }
            BotonGuia("Quemaduras")      { onAbrirGuia("quemaduras") }
            BotonGuia("Desmayos")        { onAbrirGuia("desmayos") }
            BotonGuia("Atragantamiento") { onAbrirGuia("atragantamiento") }
        }
    }
}

@Composable
private fun BotonGuia(texto: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(
                color = Color(0xFFF8F8F8),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 18.dp, horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPantallaPrimerosAuxilios() {
    val nav = rememberNavController()
    PantallaPrimerosAuxilios(
        navController = nav,
        onVolver = {},
        onAbrirGuia = {}
    )
}

