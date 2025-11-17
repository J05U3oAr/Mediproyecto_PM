//Programación de plataformas moviles
//Sebastian Lemus (241155)
//Luis Hernández (241424)
//Arodi Chavez (241112)
//prof. Juan Carlos Durini
package com.example.proyecto_1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Base de datos principal de la aplicación usando Room
//Room es una biblioteca de persistencia que proporciona una capa de abstracción sobre SQLite para acceder a la base de datos de forma más robusta.
//Entidades:
//Usuario: Información de login y cuenta
//PerfilMedico: Información médica del usuario
//Versión: 2 Usa migración destructiva (borra y recrea la BD en cambios de versión)
@Database(
    entities = [Usuario::class, PerfilMedico::class], // Tablas de la BD
    version = 2,                                       // Versión de la BD
    exportSchema = false                               // No exportar esquema
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs (Data Access Objects) para acceder a las tablas
    abstract fun usuarioDao(): UsuarioDao
    abstract fun perfilMedicoDao(): PerfilMedicoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //Obtiene la instancia única de la base de datos (Patrón Singleton)
        //Garantiza que solo exista una instancia de la BD en toda la aplicación
        //para evitar problemas de concurrencia y rendimiento.
        //@param context Contexto de la aplicación
        //@return Instancia de AppDatabase
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "husl_database"  // Nombre del archivo de la BD
                )
                    .fallbackToDestructiveMigration()  // Borra y recrea si cambia versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}