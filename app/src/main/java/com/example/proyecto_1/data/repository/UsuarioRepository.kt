package com.example.proyecto_1.data.repository

import com.example.proyecto_1.data.database.Usuario
import com.example.proyecto_1.data.database.UsuarioDao
import java.security.MessageDigest

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    /**
     * Registra un nuevo usuario
     * @return Pair<Boolean, String> - (éxito, mensaje)
     */
    suspend fun registrarUsuario(email: String, nombre: String, password: String): Pair<Boolean, String> {
        return try {
            // Verificar si el usuario ya existe
            if (usuarioDao.existeUsuario(email) > 0) {
                return Pair(false, "Este correo ya está registrado")
            }

            // Encriptar la contraseña
            val passwordEncriptada = encriptarPassword(password)

            // Crear y guardar el usuario
            val nuevoUsuario = Usuario(
                email = email,
                nombre = nombre,
                password = passwordEncriptada
            )

            usuarioDao.insertarUsuario(nuevoUsuario)
            Pair(true, "Registro exitoso")
        } catch (e: Exception) {
            Pair(false, "Error al registrar: ${e.message}")
        }
    }

    /**
     * Valida las credenciales del usuario
     * @return Pair<Boolean, Usuario?> - (éxito, usuario)
     */
    suspend fun iniciarSesion(email: String, password: String): Pair<Boolean, Usuario?> {
        return try {
            val passwordEncriptada = encriptarPassword(password)
            val usuario = usuarioDao.validarCredenciales(email, passwordEncriptada)

            if (usuario != null) {
                Pair(true, usuario)
            } else {
                Pair(false, null)
            }
        } catch (e: Exception) {
            Pair(false, null)
        }
    }

    /**
     * Obtiene un usuario por su email
     */
    suspend fun obtenerUsuario(email: String): Usuario? {
        return try {
            usuarioDao.obtenerUsuarioPorEmail(email)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Verifica si un email ya está registrado
     */
    suspend fun existeEmail(email: String): Boolean {
        return try {
            usuarioDao.existeUsuario(email) > 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Encripta la contraseña usando SHA-256
     */
    private fun encriptarPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}