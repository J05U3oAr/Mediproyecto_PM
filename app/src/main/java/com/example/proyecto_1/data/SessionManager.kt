package com.example.proyecto_1.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestor de sesión que maneja el estado de autenticación del usuario
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREF_NAME = "HusL_Session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_PROFILE_COMPLETED = "profile_completed"
        private const val KEY_HAS_MEDICAL_PROFILE = "has_medical_profile"
    }

    /**
     * Guarda la sesión del usuario después del login
     */
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

    /**
     * Marca el perfil como completado
     */
    fun markProfileCompleted() {
        prefs.edit().apply {
            putBoolean(KEY_PROFILE_COMPLETED, true)
            putBoolean(KEY_HAS_MEDICAL_PROFILE, true)
            apply()
        }
    }

    /**
     * Verifica si el usuario está logueado
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Verifica si el perfil está completado
     */
    fun isProfileCompleted(): Boolean {
        return prefs.getBoolean(KEY_PROFILE_COMPLETED, false)
    }

    /**
     * Verifica si tiene perfil médico guardado
     */
    fun hasMedicalProfile(): Boolean {
        return prefs.getBoolean(KEY_HAS_MEDICAL_PROFILE, false)
    }

    /**
     * Obtiene el email del usuario
     */
    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }

    /**
     * Obtiene el nombre del usuario
     */
    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }

    /**
     * Cierra la sesión del usuario
     */
    fun logout() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }
}