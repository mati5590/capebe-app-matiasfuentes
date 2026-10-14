package com.example.capebe.data.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// CORRECCIÓN 1: Cambiar myappnavigation por capebe
import com.example.capebe.data.local.dao.UsuarioDao
import com.example.capebe.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {          // propio del patrón singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    // CORRECCIÓN 2: Cambiar el nombre de la BD
                    "capebe_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}