//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

//Modelo de datos para el usuario registrado
//Almacena toda la información médica del usuario:
//Datos personales (nombre, edad, género)
//Información médica (tipo de sangre, alergias)
//Contacto de emergencia

data class UsuarioRegistro(
    var nombre: String = "",                    // Nombre completo del usuario
    var edad: String = "",                      // Edad en años
    var genero: String = "",                    // Masculino/Femenino/Otro
    var tipoSangre: String = "",                // A+, A-, B+, B-, AB+, AB-, O+, O-
    var alergias: String = "",                  // Lista de alergias separadas por comas
    var contactoEmergenciaNombre: String = "",  // Nombre del contacto de emergencia
    var contactoEmergenciaNumero: String = ""   // Teléfono del contacto de emergencia
)

//Modelo para recordatorios médicos
//Representa una notificación programada para:
//Tomar medicamentos
//Citas médicas
//Cualquier evento médico importante
data class RecordatorioMedico(
    val id: Int,        // Identificador único del recordatorio
    val titulo: String, // Nombre del medicamento o cita
    val hora: String,   // Hora en formato "8:00 a.m." o "5:00 p.m."
    val dia: Int,       // Día del mes (1-31)
    val mes: Int,       // Mes (1-12)
    val anio: Int       // Año (ej: 2025)
)

//Modelo para contactos de emergencia
//Almacena información de personas a contactar en caso de emergencia
data class ContactoEmergencia(
    val id: Int,            // Identificador único
    val nombre: String,     // Nombre completo del contacto
    val telefono: String,   // Número de teléfono
    val relacion: String    // Relación con el usuario (familiar, médico, amigo)
)

//SINGLETON - Gestor central de datos de la aplicación
//Este objeto maneja todos los datos en memoria de la aplicación:
//Perfil del usuario
//Lista de recordatorios médicos
//Lista de contactos de emergencia
//Utiliza Compose State para que la UI se actualice automáticamente
//cuando los datos cambien.
object AppDataManager {

    //Datos del usuario actual
    //MutableState permite que Compose observe cambios y actualice la UI
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


    //Lista de recordatorios médicos
    //Inicialmente vacía, el usuario puede agregar recordatorios

    val recordatorios = mutableStateListOf<RecordatorioMedico>()

    //Lista de contactos de emergencia
    //Incluye 3 contactos por defecto como ejemplo
    val contactos = mutableStateListOf(
        ContactoEmergencia(1, "Dr. García", "5551-2345", "Médico de cabecera"),
        ContactoEmergencia(2, "María López", "5551-6789", "Familiar"),
        ContactoEmergencia(3, "Pedro Martínez", "5551-9876", "Amigo cercano")
    )

    //Actualiza el perfil completo del usuario
    //@param usuario Nuevo objeto UsuarioRegistro con los datos actualizados
    fun actualizarUsuario(usuario: UsuarioRegistro) {
        usuarioRegistro.value = usuario.copy()
    }


    //Agrega un nuevo recordatorio a la lista
    //@param recordatorio RecordatorioMedico a agregar
    fun agregarRecordatorio(recordatorio: RecordatorioMedico) {
        recordatorios.add(recordatorio)
    }

    //Elimina un recordatorio por su ID
    //@param id Identificador del recordatorio a eliminar
    fun eliminarRecordatorio(id: Int) {
        recordatorios.removeAll { it.id == id }
    }

    //Agrega un nuevo contacto de emergencia
    //@param contacto ContactoEmergencia a agregar
    fun agregarContacto(contacto: ContactoEmergencia) {
        contactos.add(contacto)
    }

    //Elimina un contacto por su ID
    //@param id Identificador del contacto a eliminar
    fun eliminarContacto(id: Int) {
        contactos.removeAll { it.id == id }
    }

    //Resetea todos los datos a su estado inicial
    //Útil cuando el usuario cierra sesión:
    //Restaura el perfil a "Invitado"
    //Limpia todos los recordatorios
    //Restaura los contactos por defecto
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