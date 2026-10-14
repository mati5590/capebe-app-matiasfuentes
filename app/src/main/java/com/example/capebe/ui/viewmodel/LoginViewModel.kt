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
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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






    // ... el resto de tu LoginViewModel ...

    fun onBiometricLoginClick(
        context: Context, // <-- Nuevo parámetro para acceder a la memoria
        activity: FragmentActivity,
        onSuccess: (com.example.capebe.data.local.entity.UsuarioEntity) -> Unit
    ) {
        // Leemos la memoria del teléfono para ver si hay alguien guardado
        val sharedPref = context.getSharedPreferences("CAPEBE_PREFS", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("USER_EMAIL", null)

        // Si no hay correo guardado, significa que es la primera vez que abre la app
        if (savedEmail == null) {
            errorMessage = "Primero debes iniciar sesión con tu contraseña para activar la huella."
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    errorMessage = "Cancelado: $errString"
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    errorMessage = "¡Huella reconocida!"

                    // Ahora SÍ reconstruimos al usuario real guardado en memoria
                    val savedUser = com.example.capebe.data.local.entity.UsuarioEntity(
                        id = sharedPref.getInt("USER_ID", 1),
                        nombreCompleto = sharedPref.getString("USER_NAME", "Socio") ?: "Socio",
                        rut = sharedPref.getString("USER_RUT", "") ?: "",
                        email = savedEmail,
                        password = "", // No necesitamos la contraseña aquí
                        categoria = sharedPref.getString("USER_CAT", "Socio") ?: "Socio"
                    )

                    onSuccess(savedUser) // Lo mandamos al Home con sus datos reales
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    errorMessage = "Huella no reconocida."
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Iniciar sesión en CAPEBE")
            .setSubtitle("Inicia sesión como $savedEmail")
            .setNegativeButtonText("Usar contraseña")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}