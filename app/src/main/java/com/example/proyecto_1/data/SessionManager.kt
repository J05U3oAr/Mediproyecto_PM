//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data

import android.content.Context
import android.content.SharedPreferences


//Gestor de sesión del usuario
//Maneja el estado de autenticación y perfil del usuario usando SharedPreferences
//para persistir la información incluso cuando la app se cierra.
//Funcionalidades:
//Guardar/verificar estado de login
//Almacenar datos básicos del usuario
//Verificar si el perfil médico está completo
//Cerrar sesión
class SessionManager(context: Context) {

    // SharedPreferences para almacenar datos de forma persistente
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        // Nombre del archivo de preferencias
        private const val PREF_NAME = "HusL_Session"

        // Keys para almacenar datos
        private const val KEY_IS_LOGGED_IN = "is_logged_in"           //Está logueado o no
        private const val KEY_USER_EMAIL = "user_email"                // Email del usuario
        private const val KEY_USER_NAME = "user_name"                  // Nombre del usuario
        private const val KEY_PROFILE_COMPLETED = "profile_completed"  // ¿Perfil completo?
        private const val KEY_HAS_MEDICAL_PROFILE = "has_medical_profile" // ¿Tiene perfil médico?
    }

    //Guarda la sesión del usuario después del login
    //@param email Email del usuario
    //@param name Nombre del usuario (opcional)
    //@param hasMedicalProfile Si el usuario ya tiene perfil médico guardado
    fun saveLoginSession(email: String, name: String = "", hasMedicalProfile: Boolean = false) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putBoolean(KEY_HAS_MEDICAL_PROFILE, hasMedicalProfile)
            putBoolean(KEY_PROFILE_COMPLETED, hasMedicalProfile)
            apply()
        }
    }

     //Marca el perfil médico como completado
     //Se llama cuando el usuario termina de llenar su información médica
    fun markProfileCompleted() {
        prefs.edit().apply {
            putBoolean(KEY_PROFILE_COMPLETED, true)
            putBoolean(KEY_HAS_MEDICAL_PROFILE, true)
            apply()
        }
    }


     //Verifica si el usuario está logueado
     //@return true si el usuario ha iniciado sesión, false en caso contrario
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    //Verifica si el perfil del usuario está completado
    //@return true si el perfil está completo, false si falta información
    fun isProfileCompleted(): Boolean {
        return prefs.getBoolean(KEY_PROFILE_COMPLETED, false)
    }

    //Verifica si el usuario tiene perfil médico guardado en la base de datos
    //@return true si tiene perfil médico, false en caso contrario
    fun hasMedicalProfile(): Boolean {
        return prefs.getBoolean(KEY_HAS_MEDICAL_PROFILE, false)
    }

    //Obtiene el email del usuario
    //@return Email del usuario o string vacío si no existe
    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }

    //Obtiene el nombre del usuario
    //@return Nombre del usuario o string vacío si no existe
    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }

    //Cierra la sesión del usuario
    //Elimina todos los datos guardados en SharedPreferences
    fun logout() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }
}