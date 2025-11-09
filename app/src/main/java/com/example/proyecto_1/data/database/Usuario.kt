package com.example.proyecto_1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val email: String,
    val nombre: String,
    val password: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)