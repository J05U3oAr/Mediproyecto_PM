//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entidad Usuario - Tabla "usuarios" en la base de datos

//Almacena la información de autenticación del usuario:
//Email (identificador único)
//Nombre
//Contraseña encriptada
//Fecha de registro

//Esta entidad se usa para el sistema de login/registro
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val email: String,              // Email
    val nombre: String,             // Nombre completo del usuario
    val password: String,           // Contraseña encriptada con SHA-256
    val fechaRegistro: Long = System.currentTimeMillis() // Timestamp de creación
)