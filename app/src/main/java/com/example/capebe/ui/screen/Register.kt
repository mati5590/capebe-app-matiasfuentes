package com.example.capebe.ui.screen



import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.capebe.ui.viewmodel.RegisterViewModel

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onLoginClick,
                    modifier = Modifier.offset(x = (-12).dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver al inicio de sesión",
                        tint = Color(0xFF0C2E5C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0C2E5C))
                ) {
                    Icon(
                        imageVector = Icons.Default.Flight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CAPEBE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C2E5C)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Crea tu cuenta", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0C2E5C))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Regístrate para participar en las actividades de CAPEBE", fontSize = 14.sp, color = Color(0xFF5A5A5A))

            Spacer(modifier = Modifier.height(28.dp))

            Text("Nombre completo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            OutlinedTextField(
                value = uiState.nombreCompleto,
                onValueChange = { viewModel.onNombreCompletoChanged(it) },
                placeholder = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF0C2E5C)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true, // Solución implementada
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next) // Avanza al siguiente
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("RUT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            OutlinedTextField(
                value = uiState.rut,
                onValueChange = { viewModel.onRutChanged(it) },
                placeholder = { Text("RUT") },
                leadingIcon = { Icon(Icons.Default.Badge, null, tint = Color(0xFF0C2E5C)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true, // Solución implementada
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next) // Avanza al siguiente
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Correo electrónico", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF0C2E5C)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true, // Solución implementada
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next) // Avanza al siguiente
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Contraseña", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF0C2E5C)) },
                trailingIcon = {
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                },
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true, // Solución implementada
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next) // Avanza al siguiente
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Confirmar contraseña", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                placeholder = { Text("Confirmar contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF0C2E5C)) },
                trailingIcon = {
                    IconButton(onClick = { confirmPassVisible = !confirmPassVisible }) {
                        Icon(if (confirmPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                },
                visualTransformation = if (confirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true, // Solución implementada
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done) // Cierra el teclado
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text("Categoría del participante", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (uiState.categoria == "Socio CAPEBE") Color(0xFFE8F1FA) else Color.Transparent)
                    .clickable { viewModel.onCategoriaChanged("Socio CAPEBE") }
                    .padding(12.dp)
            ) {
                RadioButton(
                    selected = (uiState.categoria == "Socio CAPEBE"),
                    onClick = { viewModel.onCategoriaChanged("Socio CAPEBE") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0C2E5C))
                )
                Text("Socio CAPEBE", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0C2E5C))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (uiState.categoria == "Beneficiario") Color(0xFFE8F1FA) else Color.Transparent)
                    .clickable { viewModel.onCategoriaChanged("Beneficiario") }
                    .padding(12.dp)
            ) {
                RadioButton(
                    selected = (uiState.categoria == "Beneficiario"),
                    onClick = { viewModel.onCategoriaChanged("Beneficiario") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0C2E5C))
                )
                Text("Beneficiario de Bienestar BancoEstado", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0C2E5C))
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.terminosAceptados,
                    onCheckedChange = { viewModel.onTerminosChanged(it) },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0C2E5C))
                )
                Text(
                    text = "Acepto los Términos y condiciones y la Política de privacidad",
                    fontSize = 11.sp,
                    color = Color(0xFF5A5A5A),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onRegisterClick(context, onSuccess = onSuccess)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2E5C)),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text("Crear cuenta", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿Ya tienes una cuenta? ", fontSize = 13.sp, color = Color(0xFF5A5A5A))
                TextButton(onClick = onLoginClick, contentPadding = PaddingValues(0.dp)) {
                    Text("Inicia sesión", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}