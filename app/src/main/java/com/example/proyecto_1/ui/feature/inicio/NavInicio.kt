package com.example.proyecto_1.ui.feature.inicio

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyecto_1.ui.feature.calendario.Calendario
import com.example.proyecto_1.ui.feature.llamadas.Llamadas
import com.example.proyecto_1.ui.feature.mapa.MapaContactos
import com.example.proyecto_1.ui.feature.primerosauxilios.PrimerosAuxilios
import com.example.proyecto_1.ui.feature.registro.Registro

fun NavGraphBuilder.registrarGrafoInicio(nav: NavController) {
    composable<Inicio> {
        PantallaInicio(
            onIrPrimerosAuxilios = { nav.navigate(PrimerosAuxilios) },
            onIrMapa            = { nav.navigate(MapaContactos) },
            onIrLlamadas        = { nav.navigate(Llamadas) },
            onIrCalendario      = { nav.navigate(Calendario) }, // 👈 AHORA va al Calendario
            onIrRegistro        = { nav.navigate(Registro) },   // (por si usas otra tarjeta a futuro)
            navController       = nav
        )
    }
}
