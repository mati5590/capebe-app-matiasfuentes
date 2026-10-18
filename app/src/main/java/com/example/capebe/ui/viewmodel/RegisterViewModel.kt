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

// ViewModel encargado de gestionar la lógica de registro de nuevos usuarios.
// Mantiene el estado de la interfaz gráfica (UI State), aplica validaciones de negocio
// y maneja la persistencia de datos de forma asíncrona utilizando Room (SQLite).
class RegisterViewModel : ViewModel() {

    var uiState by mutableStateOf(RegisterModel())
        private set

    fun onNombreCompletoChanged(nombre: String) { uiState = uiState.copy(nombreCompleto = nombre, errorMessage = null) }
    fun onRutChanged(rut: String) { uiState = uiState.copy(rut = rut, errorMessage = null) }
    fun onEmailChanged(email: String) { uiState = uiState.copy(email = email, errorMessage = null) }
    fun onPasswordChanged(password: String) { uiState = uiState.copy(password = password, errorMessage = null) }
    fun onConfirmPasswordChanged(confirmPassword: String) { uiState = uiState.copy(confirmPassword = confirmPassword, errorMessage = null) }
    fun onCategoriaChanged(categoria: String) { uiState = uiState.copy(categoria = categoria, errorMessage = null) }
    fun onTerminosChanged(aceptados: Boolean) { uiState = uiState.copy(terminosAceptados = aceptados, errorMessage = null) }

    // Ejecuta la validación final del formulario y, si es correcta, persiste la nueva
    // entidad de usuario en la base de datos local, verificando previamente que el correo no exista.
    fun onRegisterClick(context: Context, onSuccess: () -> Unit) {
        val state = uiState

        if (state.nombreCompleto.isBlank() || state.rut.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Por favor completa todos los campos de texto.")
            return
        }

        if (state.password != state.confirmPassword) {
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden.")
            return
        }

        if (!state.terminosAceptados) {
            uiState = uiState.copy(errorMessage = "Debes aceptar los términos y condiciones.")
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val existingUser = db.usuarioDao().getUserByEmail(state.email)

            if (existingUser != null) {
                uiState = uiState.copy(errorMessage = "El correo ya está registrado.")
            } else {
                val newUser = UsuarioEntity(
                    nombreCompleto = state.nombreCompleto,
                    rut = state.rut,
                    email = state.email,
                    password = state.password,
                    categoria = state.categoria
                )

                db.usuarioDao().registerUser(newUser)
                uiState = uiState.copy(errorMessage = null)
                onSuccess()
            }
        }
    }
}