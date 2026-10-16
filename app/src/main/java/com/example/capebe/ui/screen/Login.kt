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
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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

    var rememberMe by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }

    // BLOQUE MÁGICO: AUTO INICIO DE SESIÓN CON HUELLA AL ABRIR LA APP
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("CAPEBE_PREFS", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("USER_EMAIL", null)

        if (savedEmail != null && activity != null) {
            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val savedUser = com.example.capebe.data.local.entity.UsuarioEntity(
                            id = sharedPref.getInt("USER_ID", 1),
                            nombreCompleto = sharedPref.getString("USER_NAME", "Socio") ?: "Socio",
                            rut = sharedPref.getString("USER_RUT", "") ?: "",
                            email = savedEmail,
                            password = "",
                            categoria = sharedPref.getString("USER_CAT", "Socio") ?: "Socio"
                        )
                        Toast.makeText(context, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
                        onSuccess(savedUser)
                    }
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
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

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    // CONTENEDOR PRINCIPAL: Fondo Azul Oscuro sin la línea punteada
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C2E5C))
            .drawBehind {
                // Dibujamos SOLO los círculos del radar
                val circleCenter = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.2f)

                drawCircle(color = Color.White.copy(alpha = 0.03f), radius = size.height * 0.4f, center = circleCenter)
                drawCircle(color = Color.White.copy(alpha = 0.05f), radius = size.height * 0.25f, center = circleCenter)
                drawCircle(color = Color.White.copy(alpha = 0.08f), radius = size.height * 0.12f, center = circleCenter)
            }
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // SECCIÓN SUPERIOR: Logo y Avión
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 28.dp, vertical = 40.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                    ) {
                        Icon(imageVector = Icons.Default.Flight, contentDescription = "Vuelo", tint = Color(0xFF0C2E5C), modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "CAPEBE", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = 2.sp)
                        Text(text = "Club Aéreo del Personal de BancoEstado", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                }

                Icon(
                    imageVector = Icons.Default.Flight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = 70.dp)
                )
            }

            // SECCIÓN INFERIOR: Formulario Blanco Redondeado
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color(0xFFFAFAFA)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(text = "¡Bienvenido a CAPEBE!", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0C2E5C))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Accede a tu cuenta y prepárate para tu próxima experiencia de vuelo", fontSize = 14.sp, color = Color(0xFF5A5A5A), lineHeight = 20.sp)

                    Spacer(modifier = Modifier.height(36.dp))

                    // Input: Correo
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.onEmailChanged(it) },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Color(0xFF0C2E5C)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0C2E5C),
                            unfocusedBorderColor = Color(0xFF0C2E5C),
                            focusedLabelColor = Color(0xFF0C2E5C),
                            unfocusedLabelColor = Color(0xFF0C2E5C)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Input: Contraseña
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.onPasswordChanged(it) },
                        label = { Text("Contraseña", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = Color.Gray)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0C2E5C),
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Fila: Recordarme y Olvidaste contraseña
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0C2E5C), checkmarkColor = Color.White)
                            )
                            Text(text = "Recordarme", fontSize = 13.sp, color = Color(0xFF222222))
                        }
                        TextButton(onClick = {  }) {
                            Text(text = "¿Olvidaste tu contraseña?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón Principal
                    Button(
                        onClick = { viewModel.onLoginClick(context, onSuccess = onSuccess) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2E5C)),
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Text(text = "Iniciar sesión", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    // Botón de Huella
                    Spacer(modifier = Modifier.height(12.dp))
                    if (activity != null) {
                        OutlinedButton(
                            onClick = { viewModel.onBiometricLoginClick(context, activity, onSuccess) },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(26.dp),
                            border = BorderStroke(1.dp, Color(0xFF0C2E5C))
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = "Huella Digital", tint = Color(0xFF0C2E5C))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingresar con Huella", color = Color(0xFF0C2E5C), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(30.dp))

                    // Enlace de Registro
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "¿No tienes una cuenta? ", fontSize = 13.sp, color = Color(0xFF5A5A5A))
                        TextButton(onClick = onRegisterClick, contentPadding = PaddingValues(0.dp)) {
                            Text(text = "Regístrate", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}