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

//DAO para la tabla Usuario

//Define todas las operaciones que se pueden realizar con la tabla de usuarios:
//Insertar usuarios
//Validar credenciales
//Verificar existencia
//Eliminar usuarios

//Todas las funciones son suspend (corrutinas) para no bloquear el hilo principal
@Dao
interface UsuarioDao {

    //Inserta un nuevo usuario en la base de datos
    //Si el email ya existe, reemplaza el usuario existente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)


    //Obtiene un usuario por su email
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?

    //Valida las credenciales de login
    //Busca un usuario con el email y contraseña proporcionados
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validarCredenciales(email: String, password: String): Usuario?

    //Verifica si un email ya está registrado
    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    suspend fun existeUsuario(email: String): Int

    //Obtiene todos los usuarios registrados
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodosLosUsuarios(): List<Usuario>

    //Elimina un usuario por su email
    @Query("DELETE FROM usuarios WHERE email = :email")
    suspend fun eliminarUsuario(email: String)
}