package com.example.proyecto_1.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

// Modelo de datos del usuario
data class UsuarioRegistro(
    var nombre: String = "",
    var edad: String = "",
    var genero: String = "",
    var tipoSangre: String = "",
    var alergias: String = "",
    var contactoEmergenciaNombre: String = "",
    var contactoEmergenciaNumero: String = ""
)

// Modelo de recordatorio médico
data class RecordatorioMedico(
    val id: Int,
    val titulo: String,
    val hora: String,
    val dia: Int,
    val mes: Int,
    val anio: Int
)

// Modelo de contacto de emergencia
data class ContactoEmergencia(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val relacion: String
)

// Singleton para manejar todos los datos de la app
object AppDataManager {

    // Datos del usuario
    val usuarioRegistro = mutableStateOf(
        UsuarioRegistro(
            nombre = "Invitado",
            edad = "",
            genero = "",
            tipoSangre = "",
            alergias = "",
            contactoEmergenciaNombre = "",
            contactoEmergenciaNumero = ""
        )
    )

    // Lista de recordatorios médicos - AHORA VACÍA POR DEFECTO
    val recordatorios = mutableStateListOf<RecordatorioMedico>()

    // Lista de contactos de emergencia
    val contactos = mutableStateListOf(
        ContactoEmergencia(1, "Dr. García", "5551-2345", "Médico de cabecera"),
        ContactoEmergencia(2, "María López", "5551-6789", "Familiar"),
        ContactoEmergencia(3, "Pedro Martínez", "5551-9876", "Amigo cercano")
    )

    // Función para actualizar el registro del usuario
    fun actualizarUsuario(usuario: UsuarioRegistro) {
        usuarioRegistro.value = usuario.copy()
    }

    // Función para agregar recordatorio
    fun agregarRecordatorio(recordatorio: RecordatorioMedico) {
        recordatorios.add(recordatorio)
    }

    // Función para eliminar recordatorio
    fun eliminarRecordatorio(id: Int) {
        recordatorios.removeAll { it.id == id }
    }

    // Función para agregar contacto
    fun agregarContacto(contacto: ContactoEmergencia) {
        contactos.add(contacto)
    }

    // Función para eliminar contacto
    fun eliminarContacto(id: Int) {
        contactos.removeAll { it.id == id }
    }

    // Función para resetear todos los datos (útil para cerrar sesión)
    fun resetearDatos() {
        usuarioRegistro.value = UsuarioRegistro(
            nombre = "Invitado",
            edad = "",
            genero = "",
            tipoSangre = "",
            alergias = "",
            contactoEmergenciaNombre = "",
            contactoEmergenciaNumero = ""
        )
        recordatorios.clear()
        contactos.clear()
        contactos.addAll(
            listOf(
                ContactoEmergencia(1, "Dr. García", "5551-2345", "Médico de cabecera"),
                ContactoEmergencia(2, "María López", "5551-6789", "Familiar"),
                ContactoEmergencia(3, "Pedro Martínez", "5551-9876", "Amigo cercano")
            )
        )
    }
}