package com.example.capebe.ui.screen




import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capebe.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    viewModel: RegisterViewModel,
    onLoginClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current

    var passVisible by remember { mutableStateOf(false) }
    var confirmPassVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    // CONTENEDOR RAÍZ
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ==========================================
            // 1. ENCABEZADO FIJO (Minimalista)
            // ==========================================
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Solo Botón Atrás
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F1FA))
                                .clickable { onLoginClick() }
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color(0xFF0C2E5C))
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text("Crea tu cuenta", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0C2E5C))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Regístrate para participar en las actividades de CAPEBE",
                        fontSize = 14.sp,
                        color = Color(0xFF5A5A5A),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp)) // Espacio de seguridad antes del formulario
                }
            }

            // ==========================================
            // 2. FORMULARIO MÓVIL (Solo esto hace scroll)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Toma todo el espacio disponible en medio
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Input: Nombre Completo
                OutlinedTextField(
                    value = uiState.nombreCompleto,
                    onValueChange = { viewModel.onNombreCompletoChanged(it) },
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Outlined.Person, null, tint = Color.Gray) },
                    trailingIcon = {
                        if (uiState.nombreCompleto.isNotBlank()) {
                            Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = Color(0xFF198754))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input: RUT
                OutlinedTextField(
                    value = uiState.rut,
                    onValueChange = { viewModel.onRutChanged(it) },
                    label = { Text("RUT", color = Color(0xFF0C2E5C)) },
                    leadingIcon = { Icon(Icons.Outlined.Badge, null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C),
                        unfocusedBorderColor = Color(0xFF0C2E5C),
                        focusedLabelColor = Color(0xFF0C2E5C),
                        unfocusedLabelColor = Color(0xFF0C2E5C)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input: Correo
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("Correo electrónico", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Email, null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input: Contraseña
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Contraseña", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = { passVisible = !passVisible }) {
                            Icon(if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray)
                        }
                    },
                    visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input: Confirmar Contraseña
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                    label = { Text("Confirmar contraseña", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPassVisible = !confirmPassVisible }) {
                            Icon(if (confirmPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray)
                        }
                    },
                    visualTransformation = if (confirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Categoría del participante
                Text("Categoría del participante", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
                Spacer(modifier = Modifier.height(12.dp))

                val isSocio = uiState.categoria == "Socio CAPEBE"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSocio) Color(0xFFE8F1FA) else Color.Transparent)
                        .border(
                            width = if (isSocio) 1.5.dp else 1.dp,
                            color = if (isSocio) Color(0xFF0C2E5C) else Color(0xFFCCCCCC),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.onCategoriaChanged("Socio CAPEBE") }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSocio) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isSocio) Color(0xFF0C2E5C) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Socio CAPEBE", fontSize = 14.sp, fontWeight = if (isSocio) FontWeight.Bold else FontWeight.Normal, color = if (isSocio) Color(0xFF0C2E5C) else Color(0xFF5A5A5A))
                    }
                    if (isSocio) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val isBeneficiario = uiState.categoria == "Beneficiario"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isBeneficiario) Color(0xFFE8F1FA) else Color.Transparent)
                        .border(
                            width = if (isBeneficiario) 1.5.dp else 1.dp,
                            color = if (isBeneficiario) Color(0xFF0C2E5C) else Color(0xFFCCCCCC),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.onCategoriaChanged("Beneficiario") }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isBeneficiario) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isBeneficiario) Color(0xFF0C2E5C) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Beneficiario de Bienestar BancoEstado", fontSize = 14.sp, fontWeight = if (isBeneficiario) FontWeight.Bold else FontWeight.Normal, color = if (isBeneficiario) Color(0xFF0C2E5C) else Color(0xFF5A5A5A))
                    }
                    if (isBeneficiario) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Checkbox Términos
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = uiState.terminosAceptados,
                        onCheckedChange = { viewModel.onTerminosChanged(it) },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0C2E5C), checkmarkColor = Color.White)
                    )
                    Text(
                        text = "Acepto los Términos y condiciones y la Política de privacidad",
                        fontSize = 12.sp,
                        color = Color(0xFF5A5A5A),
                        modifier = Modifier.padding(start = 4.dp),
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Principal
                Button(
                    onClick = { viewModel.onRegisterClick(context, onSuccess = onSuccess) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(28.dp), spotColor = Color(0xFF0C2E5C)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF092A59), Color(0xFF194A8D))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Crear cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp)) // Espaciado inferior de la lista
            }

            // ==========================================
            // 3. PIE FIJO (No se mueve con el scroll)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿Ya tienes una cuenta? ", fontSize = 14.sp, color = Color(0xFF5A5A5A))
                Text(
                    text = "Inicia sesión",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C2E5C),
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}