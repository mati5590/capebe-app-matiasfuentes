package com.example.capebe.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capebe.data.database.AppDatabase // Asegúrate de que esta ruta coincida con tu clase Room
import com.example.capebe.model.LoginModel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginModel())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
        errorMessage = null
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password)
        errorMessage = null
    }

    // Ahora devuelve el UsuarioEntity completo
    fun onLoginClick(context: android.content.Context, onSuccess: (com.example.capebe.data.local.entity.UsuarioEntity) -> Unit) {
        val email = uiState.email
        val password = uiState.password

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa correo y contraseña."
            return
        }

        viewModelScope.launch {
            // Quitamos la ruta larga. Ahora Android Studio deberá importarlo.
            val db = AppDatabase.getDatabase(context)
            val user = db.usuarioDao().getUserByEmail(email)

            if (user != null && user.password == password) {
                errorMessage = null
                onSuccess(user)
            } else {
                errorMessage = "Correo o contraseña incorrectos."
            }
        }
    }
}