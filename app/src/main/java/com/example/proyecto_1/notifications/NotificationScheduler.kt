package com.example.proyecto_1.notifications

import android.content.Context
import androidx.work.*
import com.example.proyecto_1.data.RecordatorioMedico
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun programarNotificacion(context: Context, recordatorio: RecordatorioMedico) {
        // Calcular el tiempo hasta la notificación
        val ahora = Calendar.getInstance()
        val fechaRecordatorio = Calendar.getInstance().apply {
            set(Calendar.YEAR, recordatorio.anio)
            set(Calendar.MONTH, recordatorio.mes - 1) // Los meses en Calendar van de 0 a 11
            set(Calendar.DAY_OF_MONTH, recordatorio.dia)

            // Parsear la hora (formato: "8:00 a.m." o "5:00 p.m.")
            val horaTexto = recordatorio.hora
            val (hora, minuto) = parsearHora(horaTexto)
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minuto)
            set(Calendar.SECOND, 0)
        }

        // Si la fecha ya pasó, no programar
        if (fechaRecordatorio.before(ahora)) {
            return
        }

        val tiempoHastaNotificacion = fechaRecordatorio.timeInMillis - ahora.timeInMillis

        // Crear los datos para el Worker
        val data = Data.Builder()
            .putString("titulo", recordatorio.titulo)
            .putString("mensaje", "Es hora de: ${recordatorio.titulo}")
            .putInt("recordatorioId", recordatorio.id)
            .build()

        // Crear la solicitud de trabajo
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(tiempoHastaNotificacion, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("recordatorio_${recordatorio.id}")
            .build()

        // Programar el trabajo
        WorkManager.getInstance(context).enqueueUniqueWork(
            "recordatorio_${recordatorio.id}",
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
    }

    fun cancelarNotificacion(context: Context, recordatorioId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("recordatorio_$recordatorioId")
    }

    fun cancelarTodasLasNotificaciones(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }

    private fun parsearHora(horaTexto: String): Pair<Int, Int> {
        try {
            // Ejemplo: "8:00 a.m." o "5:00 p.m."
            val partes = horaTexto.trim().split(":")
            var hora = partes[0].trim().toInt()
            val minutoYPeriodo = partes[1].trim().split(" ")
            val minuto = minutoYPeriodo[0].toInt()
            val periodo = if (minutoYPeriodo.size > 1) minutoYPeriodo[1].lowercase() else ""

            // Convertir a formato 24 horas
            if (periodo.contains("p") && hora != 12) {
                hora += 12
            } else if (periodo.contains("a") && hora == 12) {
                hora = 0
            }

            return Pair(hora, minuto)
        } catch (e: Exception) {
            // Si hay error, devolver 9:00 AM por defecto
            return Pair(9, 0)
        }
    }
}