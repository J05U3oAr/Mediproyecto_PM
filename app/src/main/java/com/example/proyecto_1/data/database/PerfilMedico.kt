//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entidad PerfilMedico - Tabla "perfiles_medicos" en la base de datos

//Almacena toda la información médica del usuario:
//Datos personales (nombre, edad, género)
//Información médica (tipo de sangre, alergias)
//Contacto de emergencia

//Se vincula al Usuario mediante el email
@Entity(tableName = "perfiles_medicos")
data class PerfilMedico(
    @PrimaryKey
    val emailUsuario: String,              // Email del usuario
    val nombre: String,                    // Nombre completo
    val edad: String,                      // Edad en años
    val genero: String,                    // Masculino/Femenino/Otro
    val tipoSangre: String,                // A+, A-, B+, B-, AB+, AB-, O+, O-
    val alergias: String,                  // Lista de alergias
    val contactoEmergenciaNombre: String,  // Nombre del contacto de emergencia
    val contactoEmergenciaNumero: String,  // Teléfono del contacto
    val fechaActualizacion: Long = System.currentTimeMillis() // Timestamp última actualización
)