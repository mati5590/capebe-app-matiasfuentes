package com.example.capebe.ui.screen



import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.capebe.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onSuccess: (com.example.capebe.data.local.entity.UsuarioEntity) -> Unit
) {
    val uiState = viewModel.uiState
    val errorMessage = viewModel.errorMessage
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // BLOQUE MÁGICO: AUTO INICIO DE SESIÓN CON HUELLA AL ABRIR LA APP
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("CAPEBE_PREFS", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("USER_EMAIL", null)

        // Si hay un correo guardado, pedir la huella automáticamente
        if (savedEmail != null && activity != null) {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)

                        // Reconstruimos al usuario real que estaba guardado
                        val savedUser = com.example.capebe.data.local.entity.UsuarioEntity(
                            id = sharedPref.getInt("USER_ID", 1),
                            nombreCompleto = sharedPref.getString("USER_NAME", "Socio") ?: "Socio",
                            rut = sharedPref.getString("USER_RUT", "") ?: "",
                            email = savedEmail,
                            password = "",
                            categoria = sharedPref.getString("USER_CAT", "Socio") ?: "Socio"
                        )
                        Toast.makeText(context, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
                        onSuccess(savedUser) // Lo mandamos directo al Home
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Si cancela la huella, se queda en la pantalla normal para poner contraseña
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Bienvenido de nuevo")
                .setSubtitle("Inicia sesión como $savedEmail")
                .setNegativeButtonText("Usar contraseña")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    // Muestra errores si los hay
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(160.dp)
                .offset(x = 40.dp, y = (-40).dp)
                .clip(RoundedCornerShape(bottomStart = 120.dp))
                .background(Color(0xFFE8F1FA))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF0C2E5C))) {
                    Icon(imageVector = Icons.Default.Flight, contentDescription = "Vuelo", tint = Color.White, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "CAPEBE", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C), letterSpacing = 1.sp)
                    Text(text = "Club Aéreo del Personal de BancoEstado", fontSize = 11.sp, color = Color(0xFF5A5A5A))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text(text = "¡Bienvenido a CAPEBE!", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0C2E5C))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Accede a tu cuenta y prepárate para tu próxima experiencia de vuelo", fontSize = 14.sp, color = Color(0xFF5A5A5A), lineHeight = 20.sp)

            Spacer(modifier = Modifier.height(36.dp))
            Text(text = "Correo electrónico", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C), modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(
                value = uiState.email, onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = { Text("ejemplo@correo.com", color = Color.Gray.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF0C2E5C)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)),
                singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Contraseña", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C), modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(
                value = uiState.password, onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = { Text("Contraseña", color = Color.Gray.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF0C2E5C)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = Color.Gray)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)),
                singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0C2E5C)))
                    Text(text = "Recordarme", fontSize = 12.sp, color = Color(0xFF222222))
                }
                TextButton(onClick = {  }) { Text(text = "¿Olvidaste tu contraseña?", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0C2E5C)) }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.onLoginClick(context, onSuccess = onSuccess) },
                modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2E5C)), shape = RoundedCornerShape(26.dp)
            ) { Text(text = "Iniciar sesión", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White) }

            // Botón manual por si cancelan el cuadro automático
            Spacer(modifier = Modifier.height(12.dp))
            if (activity != null) {
                OutlinedButton(
                    onClick = { viewModel.onBiometricLoginClick(context,activity, onSuccess) },
                    modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(26.dp), border = BorderStroke(1.dp, Color(0xFF0C2E5C))
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = "Huella Digital", tint = Color(0xFF0C2E5C))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ingresar con Huella", color = Color(0xFF0C2E5C), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(30.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "¿No tienes una cuenta? ", fontSize = 13.sp, color = Color(0xFF5A5A5A))
                TextButton(onClick = onRegisterClick, contentPadding = PaddingValues(0.dp)) {
                    Text(text = "Regístrate", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}