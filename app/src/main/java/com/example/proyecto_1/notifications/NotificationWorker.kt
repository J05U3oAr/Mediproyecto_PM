//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.calendary.R
import com.example.proyecto_1.MainActivity

//Worker que ejecuta las notificaciones en segundo plano
//Este Worker es ejecutado por WorkManager cuando llega el momento
//de mostrar una notificación de recordatorio médico.
//Proceso:
 // 1. Recibe los datos del recordatorio
 // 2. Crea el canal de notificación (Android 8.0+)
 // 3. Construye la notificación
 //4. La muestra al usuario
class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    //Método que se ejecuta cuando llega el momento de la notificación
    //@return Result.success() si la notificación se mostró correctamente
    override fun doWork(): Result {
        // Obtener los datos pasados al Worker
        val titulo = inputData.getString("titulo") ?: "Recordatorio"
        val mensaje = inputData.getString("mensaje") ?: "Es hora de tu medicina"
        val recordatorioId = inputData.getInt("recordatorioId", 0)

        // Mostrar la notificación
        mostrarNotificacion(titulo, mensaje, recordatorioId)

        // Indicar que el trabajo se completó exitosamente
        return Result.success()
    }

    //Crea y muestra la notificación al usuario
    //
    //@param titulo Título de la notificación
    //@param mensaje Mensaje de la notificación
    //@param recordatorioId ID único de la notificación
    private fun mostrarNotificacion(titulo: String, mensaje: String, recordatorioId: Int) {
        // Obtener el NotificationManager del sistema
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "recordatorios_medicos"

        // Crear canal de notificación (necesario para Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios Médicos",              // Nombre visible para el usuario
                NotificationManager.IMPORTANCE_HIGH   // Importancia alta (sonido + vibración)
            ).apply {
                description = "Notificaciones para recordatorios de medicinas y citas"
                enableVibration(true)  // Activar vibración
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Crear un Intent para abrir la app cuando se toque la notificación
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // PendingIntent envuelve el Intent para uso futuro
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            recordatorioId,  // ID único para cada notificación
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación usando NotificationCompat
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // Icono pequeño
            .setContentTitle(titulo)                           // Título
            .setContentText(mensaje)                           // Mensaje
            .setPriority(NotificationCompat.PRIORITY_HIGH)     // Prioridad alta
            .setAutoCancel(true)                               // Se elimina al tocarla
            .setContentIntent(pendingIntent)                   // Acción al tocar
            .build()

        // Mostrar la notificación
        notificationManager.notify(recordatorioId, notification)
    }
}