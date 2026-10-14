package com.example.capebe.ui.viewmodel



import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capebe.data.database.AppDatabase
import com.example.capebe.data.local.entity.UsuarioEntity
import com.example.capebe.model.RegisterModel
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    var uiState by mutableStateOf(RegisterModel())
        private set

    // Funciones para actualizar cada campo del formulario
    fun onNombreCompletoChanged(nombre: String) { uiState = uiState.copy(nombreCompleto = nombre, errorMessage = null) }
    fun onRutChanged(rut: String) { uiState = uiState.copy(rut = rut, errorMessage = null) }
    fun onEmailChanged(email: String) { uiState = uiState.copy(email = email, errorMessage = null) }
    fun onPasswordChanged(password: String) { uiState = uiState.copy(password = password, errorMessage = null) }
    fun onConfirmPasswordChanged(confirmPassword: String) { uiState = uiState.copy(confirmPassword = confirmPassword, errorMessage = null) }
    fun onCategoriaChanged(categoria: String) { uiState = uiState.copy(categoria = categoria, errorMessage = null) }
    fun onTerminosChanged(aceptados: Boolean) { uiState = uiState.copy(terminosAceptados = aceptados, errorMessage = null) }

    fun onRegisterClick(context: Context, onSuccess: () -> Unit) {
        val state = uiState

        // 1. Validar que no haya campos vacíos
        if (state.nombreCompleto.isBlank() || state.rut.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Por favor completa todos los campos de texto.")
            return
        }

        // 2. Validar que las contraseñas coincidan
        if (state.password != state.confirmPassword) {
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden.")
            return
        }

        // 3. Validar términos y condiciones
        if (!state.terminosAceptados) {
            uiState = uiState.copy(errorMessage = "Debes aceptar los términos y condiciones.")
            return
        }

        // Método asíncrono en SQLite con Room
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val existingUser = db.usuarioDao().getUserByEmail(state.email)

            if (existingUser != null) {
                uiState = uiState.copy(errorMessage = "El correo ya está registrado.")
            } else {
                // Mapeamos los datos del estado a la entidad de la Base de Datos
                val newUser = UsuarioEntity(
                    nombreCompleto = state.nombreCompleto,
                    rut = state.rut,
                    email = state.email,
                    password = state.password,
                    categoria = state.categoria
                )

                db.usuarioDao().registerUser(newUser)
                uiState = uiState.copy(errorMessage = null) // Limpiamos errores
                onSuccess() // Navega o cierra la pantalla de registro
            }
        }
    }
}