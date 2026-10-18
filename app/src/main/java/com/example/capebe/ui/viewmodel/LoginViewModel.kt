package com.example.capebe.ui.viewmodel


import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capebe.data.database.AppDatabase
import com.example.capebe.model.LoginModel
import kotlinx.coroutines.launch

// ViewModel que gestiona la lógica de autenticación de la pantalla de inicio de sesión (Login).
// Controla el estado de la interfaz de usuario, valida credenciales contra la base de datos local (Room)
// y administra el flujo de autenticación biométrica leyendo datos encriptados/cacheados en SharedPreferences.
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

    // Verifica las credenciales ingresadas contra los registros de la base de datos local (Room).
    // En caso de éxito, retorna la entidad completa del usuario a través de un callback (onSuccess).
    fun onLoginClick(context: Context, onSuccess: (com.example.capebe.data.local.entity.UsuarioEntity) -> Unit) {
        val email = uiState.email
        val password = uiState.password

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa correo y contraseña."
            return
        }

        viewModelScope.launch {
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

    // Inicia el flujo de autenticación biométrica (huella dactilar o rostro).
    // Requiere un inicio de sesión manual previo exitoso para poder recuperar el usuario base desde SharedPreferences.
    fun onBiometricLoginClick(
        context: Context,
        activity: FragmentActivity,
        onSuccess: (com.example.capebe.data.local.entity.UsuarioEntity) -> Unit
    ) {
        val sharedPref = context.getSharedPreferences("CAPEBE_PREFS", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("USER_EMAIL", null)

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

                    val savedUser = com.example.capebe.data.local.entity.UsuarioEntity(
                        id = sharedPref.getInt("USER_ID", 1),
                        nombreCompleto = sharedPref.getString("USER_NAME", "Socio") ?: "Socio",
                        rut = sharedPref.getString("USER_RUT", "") ?: "",
                        email = savedEmail,
                        password = "",
                        categoria = sharedPref.getString("USER_CAT", "Socio") ?: "Socio"
                    )

                    onSuccess(savedUser)
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