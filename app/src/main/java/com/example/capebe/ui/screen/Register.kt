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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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

    // ==========================================
    // REGLAS DE VALIDACIÓN EN TIEMPO REAL
    // ==========================================
    val isNameValid = uiState.nombreCompleto.isNotBlank() && uiState.nombreCompleto.all { it.isLetter() || it.isWhitespace() }
    val isRutValid = uiState.rut.matches(Regex("^[0-9]{7,8}-[0-9Kk]$"))
    val isEmailValid = uiState.email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()
    val isPasswordValid = uiState.password.length >= 8 && uiState.password.any { it.isDigit() } && uiState.password.any { it.isLetter() }
    val isConfirmValid = uiState.confirmPassword.isNotEmpty() && uiState.confirmPassword == uiState.password && isPasswordValid

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. ENCABEZADO FIJO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .drawBehind {
                        val strokeColor = Color(0xFFF1F5F9)
                        val circleCenter = androidx.compose.ui.geometry.Offset(
                            x = size.width - 50.dp.toPx(),
                            y = 74.dp.toPx()
                        )
                        drawCircle(color = strokeColor, radius = 110.dp.toPx(), center = circleCenter, style = Stroke(width = 3f))
                    }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-24).dp, y = 36.dp)
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F1FA))
                ) {
                    Icon(
                        imageVector = Icons.Default.Flight,
                        contentDescription = null,
                        tint = Color(0xFF0C2E5C),
                        modifier = Modifier.size(36.dp).rotate(-45f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

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
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 2. FORMULARIO MÓVIL
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                        if (isNameValid) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF198754))
                    },
                    isError = uiState.nombreCompleto.isNotEmpty() && !isNameValid,
                    supportingText = {
                        if (uiState.nombreCompleto.isNotEmpty() && !isNameValid) {
                            Text("Solo se permiten letras y espacios.", color = Color(0xFFB3261E))
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

                Spacer(modifier = Modifier.height(8.dp))

                // Input: RUT
                OutlinedTextField(
                    value = uiState.rut,
                    onValueChange = { viewModel.onRutChanged(it) },
                    label = { Text("RUT", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Badge, null, tint = Color.Gray) },
                    trailingIcon = {
                        if (isRutValid) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF198754))
                    },
                    isError = uiState.rut.isNotEmpty() && !isRutValid,
                    supportingText = {
                        if (uiState.rut.isNotEmpty() && !isRutValid) {
                            Text("Debe incluir guion. Ej: 12345678-9", color = Color(0xFFB3261E))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C),
                        unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input: Correo
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("Correo electrónico", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Email, null, tint = Color.Gray) },
                    trailingIcon = {
                        if (isEmailValid) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF198754))
                    },
                    isError = uiState.email.isNotEmpty() && !isEmailValid,
                    supportingText = {
                        if (uiState.email.isNotEmpty() && !isEmailValid) {
                            Text("Ingresa un correo válido (ej: usuario@correo.com)", color = Color(0xFFB3261E))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0C2E5C), unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input: Contraseña
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Contraseña", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 4.dp)) {
                            if (isPasswordValid) {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF198754), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            IconButton(onClick = { passVisible = !passVisible }, modifier = Modifier.size(32.dp)) {
                                Icon(if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                        }
                    },
                    isError = uiState.password.isNotEmpty() && !isPasswordValid,
                    supportingText = {
                        val textColor = if (uiState.password.isNotEmpty() && !isPasswordValid) Color(0xFFB3261E) else Color.Gray
                        Text("Mínimo 8 caracteres, al menos 1 letra y 1 número.", color = textColor)
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

                Spacer(modifier = Modifier.height(8.dp))

                // Input: Confirmar Contraseña
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                    label = { Text("Confirmar contraseña", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 4.dp)) {
                            if (isConfirmValid) {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF198754), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            IconButton(onClick = { confirmPassVisible = !confirmPassVisible }, modifier = Modifier.size(32.dp)) {
                                Icon(if (confirmPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                        }
                    },
                    isError = uiState.confirmPassword.isNotEmpty() && uiState.confirmPassword != uiState.password,
                    supportingText = {
                        if (uiState.confirmPassword.isNotEmpty() && uiState.confirmPassword != uiState.password) {
                            Text("Las contraseñas no coinciden.", color = Color(0xFFB3261E))
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

                Spacer(modifier = Modifier.height(16.dp))

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
                        .padding(horizontal = 16.dp, vertical = 18.dp)
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
                        .padding(horizontal = 16.dp, vertical = 18.dp)
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
                    onClick = {
                        if (isNameValid && isRutValid && isEmailValid && isConfirmValid && uiState.terminosAceptados) {
                            viewModel.onRegisterClick(context, onSuccess = onSuccess)
                        } else {
                            Toast.makeText(context, "Por favor, completa correctamente todos los campos y acepta los términos.", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), spotColor = Color(0xFF0C2E5C)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2E5C)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Crear cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. PIE FIJO
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