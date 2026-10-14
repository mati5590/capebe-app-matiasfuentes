package com.example.capebe.model

data class RegisterModel(
    val nombreCompleto: String = "",
    val rut: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val categoria: String = "Socio CAPEBE", // Por defecto según el diseño
    val terminosAceptados: Boolean = false,
    val errorMessage: String? = null
)