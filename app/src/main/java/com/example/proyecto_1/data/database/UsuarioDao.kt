package com.example.proyecto_1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validarCredenciales(email: String, password: String): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    suspend fun existeUsuario(email: String): Int

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodosLosUsuarios(): List<Usuario>

    @Query("DELETE FROM usuarios WHERE email = :email")
    suspend fun eliminarUsuario(email: String)
}