//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//DAO para la tabla PerfilMedico

//Define operaciones CRUD para perfiles médicos:
//Insertar perfil
//Actualizar perfil
//Buscar perfil
//Verificar existencia
//Eliminar perfil
@Dao
interface PerfilMedicoDao {

    //Inserta o reemplaza un perfil médico
    //Si el email ya tiene perfil, lo reemplaza con los nuevos datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPerfil(perfil: PerfilMedico)

    //Actualiza un perfil médico existente
    @Update
    suspend fun actualizarPerfil(perfil: PerfilMedico)

    //Obtiene el perfil médico de un usuario por su email
    @Query("SELECT * FROM perfiles_medicos WHERE emailUsuario = :email LIMIT 1")
    suspend fun obtenerPerfilPorEmail(email: String): PerfilMedico?

    //Verifica si un usuario ya tiene perfil médico
    @Query("SELECT COUNT(*) FROM perfiles_medicos WHERE emailUsuario = :email")
    suspend fun existePerfil(email: String): Int

    //Elimina el perfil médico de un usuario
    @Query("DELETE FROM perfiles_medicos WHERE emailUsuario = :email")
    suspend fun eliminarPerfil(email: String)
}