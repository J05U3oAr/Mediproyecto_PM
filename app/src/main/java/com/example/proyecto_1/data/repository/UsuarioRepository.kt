//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data.repository

import com.example.proyecto_1.data.database.Usuario
import com.example.proyecto_1.data.database.UsuarioDao
import java.security.MessageDigest

//Repositorio de Usuarios

//Implementa la lógica de negocio para operaciones con usuarios:
//Registro de nuevos usuarios
//Inicio de sesión
//Encriptación de contraseñas
//Validaciones

//Actúa como capa intermedia entre la UI y la base de datos
class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    //Registra un nuevo usuario en el sistema
    //Proceso:
    //1. Verifica que el email no esté registrado
    //2. Encripta la contraseña con SHA-256
    //3. Crea el usuario en la BD

    //@param email Email del usuario (debe ser @uvg.edu.gt)
    //@param nombre Nombre completo del usuario
    //@param password Contraseña en texto plano
    //@return Pair<Boolean, String> - (éxito, mensaje)
    suspend fun registrarUsuario(email: String, nombre: String, password: String): Pair<Boolean, String> {
        return try {
            // Verificar si el usuario ya existe
            if (usuarioDao.existeUsuario(email) > 0) {
                return Pair(false, "Este correo ya está registrado")
            }

            // Encriptar la contraseña usando SHA-256
            val passwordEncriptada = encriptarPassword(password)

            // Crear el nuevo usuario
            val nuevoUsuario = Usuario(
                email = email,
                nombre = nombre,
                password = passwordEncriptada
            )

            // Guardar en la base de datos
            usuarioDao.insertarUsuario(nuevoUsuario)
            Pair(true, "Registro exitoso")
        } catch (e: Exception) {
            Pair(false, "Error al registrar: ${e.message}")
        }
    }

    //Valida las credenciales de un usuario

    //Proceso:
    //1. Encripta la contraseña ingresada
    //2. Busca en la BD un usuario con ese email y contraseña

    //@param email Email del usuario
    //@param password Contraseña en texto plano
    //@return Pair<Boolean, Usuario?> - (éxito, usuario encontrado o null)
    suspend fun iniciarSesion(email: String, password: String): Pair<Boolean, Usuario?> {
        return try {
            // Encriptar la contraseña para compararla
            val passwordEncriptada = encriptarPassword(password)

            // Buscar usuario con esas credenciales
            val usuario = usuarioDao.validarCredenciales(email, passwordEncriptada)

            if (usuario != null) {
                Pair(true, usuario)  // Credenciales correctas
            } else {
                Pair(false, null)     // Credenciales incorrectas
            }
        } catch (e: Exception) {
            Pair(false, null)
        }
    }

    //Obtiene un usuario por su email

    //@param email Email del usuario
    //@return Usuario si existe, null si no
    suspend fun obtenerUsuario(email: String): Usuario? {
        return try {
            usuarioDao.obtenerUsuarioPorEmail(email)
        } catch (e: Exception) {
            null
        }
    }

    //Verifica si un email ya está registrado

    //@param email Email a verificar
    //@return true si existe, false si no
    suspend fun existeEmail(email: String): Boolean {
        return try {
            usuarioDao.existeUsuario(email) > 0
        } catch (e: Exception) {
            false
        }
    }

    //Encripta una contraseña usando el algoritmo SHA-256

    //SHA-256 es un algoritmo de hash criptográfico
    //@param password Contraseña en texto plano
    //@return Hash de la contraseña en formato hexadecimal
    private fun encriptarPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}