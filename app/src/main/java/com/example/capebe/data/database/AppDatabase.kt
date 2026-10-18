package com.example.capebe.data.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.capebe.data.local.dao.UsuarioDao
import com.example.capebe.data.local.entity.UsuarioEntity

// Configuración principal de la base de datos local utilizando Room (SQLite).
// Implementa el patrón Singleton (a través del companion object) para asegurar que
// solo exista una única instancia de la base de datos activa en toda la aplicación,
// previniendo fugas de memoria y garantizando un acceso seguro y concurrente a los datos.
@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "capebe_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}