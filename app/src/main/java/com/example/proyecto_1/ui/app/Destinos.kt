//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.ui.app

import kotlinx.serialization.Serializable

//Definición de destinos de navegación usando Kotlin Serialization
//Cada objeto representa una pantalla de la aplicación.

//Pantallas de la aplicación:
@Serializable object Login            // Pantalla de login/registro de cuenta
@Serializable object Inicio           // Pantalla principal con menú de opciones
@Serializable object MapaContactos    // Mapa con ubicación y contactos de emergencia
@Serializable object Calendario       // Recordatorios médicos y notificaciones
@Serializable object Llamadas         // Pantalla para realizar llamadas de emergencia
@Serializable object Registro         // Formulario de perfil médico del usuario
@Serializable object PrimerosAuxilios // Guías de primeros auxilios en PDF
@Serializable object Perfil           // Perfil del usuario y cerrar sesiónfil