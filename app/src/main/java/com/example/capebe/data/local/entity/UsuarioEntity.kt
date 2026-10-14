package com.example.capebe.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users") // Se mantiene "users" para que coincida con tu DAO
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Campos basados en tu diseño de Registro
    val nombreCompleto: String,
    val rut: String,
    val email: String,
    val password: String,
    val categoria: String // Guardará "Socio CAPEBE" o "Beneficiario de Bienestar BancoEstado"
)