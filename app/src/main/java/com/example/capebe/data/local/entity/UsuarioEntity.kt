package com.example.capebe.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,


    val nombreCompleto: String,
    val rut: String,
    val email: String,
    val password: String,
    val categoria: String
)