//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.notifications

import android.content.Context
import androidx.work.*
import com.example.proyecto_1.data.RecordatorioMedico
import java.util.Calendar
import java.util.concurrent.TimeUnit

//Programador de Notificaciones

//Gestiona la programación y cancelación de notificaciones para recordatorios médicos
//usando WorkManager, que garantiza que las notificaciones se ejecuten incluso si: la app está cerrada, se reinicia el dispositivo o se optimiza la bateria

object NotificationScheduler {

    //Programa una notificación para un recordatorio médico

     //Proceso:
     // 1. Calcula el tiempo hasta la fecha/hora del recordatorio
     // 2. Si la fecha ya pasó, no programa la notificación
     // 3. Crea un trabajo (Work) con los datos del recordatorio
     // 4. Lo programa con WorkManager

    //@param context Contexto de la aplicación
    //@param recordatorio RecordatorioMedico a programar
    fun programarNotificacion(context: Context, recordatorio: RecordatorioMedico) {
        // Obtener la hora actual
        val ahora = Calendar.getInstance()

        // Configurar la fecha/hora del recordatorio
        val fechaRecordatorio = Calendar.getInstance().apply {
            set(Calendar.YEAR, recordatorio.anio)
            set(Calendar.MONTH, recordatorio.mes - 1)  // Los meses van de 0 a 11
            set(Calendar.DAY_OF_MONTH, recordatorio.dia)

            // Parsear la hora del recordatorio (ej: "8:00 a.m.")
            val (hora, minuto) = parsearHora(recordatorio.hora)
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minuto)
            set(Calendar.SECOND, 0)
        }

        // Si la fecha ya pasó, no programar la notificación
        if (fechaRecordatorio.before(ahora)) {
            return
        }

        // Calcular cuánto tiempo falta hasta la notificación
        val tiempoHastaNotificacion = fechaRecordatorio.timeInMillis - ahora.timeInMillis

        // Crear los datos que se pasarán al Worker
        val data = Data.Builder()
            .putString("titulo", recordatorio.titulo)
            .putString("mensaje", "Es hora de: ${recordatorio.titulo}")
            .putInt("recordatorioId", recordatorio.id)
            .build()

        // Crear la solicitud de trabajo (Work Request)
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(tiempoHastaNotificacion, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("recordatorio_${recordatorio.id}")
            .build()

        // Programar el trabajo con WorkManager
        // REPLACE significa que si ya existe un trabajo con ese nombre, lo reemplaza
        WorkManager.getInstance(context).enqueueUniqueWork(
            "recordatorio_${recordatorio.id}",
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
    }

    //Cancela una notificación específica
    //@param context Contexto de la aplicación
    //@param recordatorioId ID del recordatorio a cancelar
    fun cancelarNotificacion(context: Context, recordatorioId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("recordatorio_$recordatorioId")
    }

    //Cancela todas las notificaciones programadas
    //@param context Contexto de la aplicación
    fun cancelarTodasLasNotificaciones(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }

     //Parsea una hora en formato "8:00 a.m." o "5:00 p.m." a formato 24 horas
     //@param horaTexto Hora en formato texto
     //@return Pair<Int, Int> - (hora en formato 24h, minutos)
    private fun parsearHora(horaTexto: String): Pair<Int, Int> {
        try {
            // Dividir por ":" para separar hora y minutos
            val partes = horaTexto.trim().split(":")
            var hora = partes[0].trim().toInt()

            // Separar minutos del indicador AM/PM
            val minutoYPeriodo = partes[1].trim().split(" ")
            val minuto = minutoYPeriodo[0].toInt()
            val periodo = if (minutoYPeriodo.size > 1) minutoYPeriodo[1].lowercase() else ""

            // Convertir a formato 24 horas
            if (periodo.contains("p") && hora != 12) {
                // PM y no es 12: sumar 12 (ej: 5 PM -> 17)
                hora += 12
            } else if (periodo.contains("a") && hora == 12) {
                // AM y es 12: convertir a 0 (12 AM = medianoche)
                hora = 0
            }

            return Pair(hora, minuto)
        } catch (e: Exception) {
            // Si hay error al parsear, devolver 9:00 AM por defecto
            return Pair(9, 0)
        }
    }
}