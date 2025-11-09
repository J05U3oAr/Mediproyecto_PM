package com.example.proyecto_1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PerfilMedicoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPerfil(perfil: PerfilMedico)

    @Update
    suspend fun actualizarPerfil(perfil: PerfilMedico)

    @Query("SELECT * FROM perfiles_medicos WHERE emailUsuario = :email LIMIT 1")
    suspend fun obtenerPerfilPorEmail(email: String): PerfilMedico?

    @Query("SELECT COUNT(*) FROM perfiles_medicos WHERE emailUsuario = :email")
    suspend fun existePerfil(email: String): Int

    @Query("DELETE FROM perfiles_medicos WHERE emailUsuario = :email")
    suspend fun eliminarPerfil(email: String)
}