package com.example.proyecto_1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfiles_medicos")
data class PerfilMedico(
    @PrimaryKey
    val emailUsuario: String,
    val nombre: String,
    val edad: String,
    val genero: String,
    val tipoSangre: String,
    val alergias: String,
    val contactoEmergenciaNombre: String,
    val contactoEmergenciaNumero: String,
    val fechaActualizacion: Long = System.currentTimeMillis()
)