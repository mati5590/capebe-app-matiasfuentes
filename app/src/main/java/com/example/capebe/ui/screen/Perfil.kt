package com.example.capebe.ui.screen



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Perfil(
    nombreCompleto: String,
    correo: String,
    rut: String,
    categoria: String,
    onEditarClick: () -> Unit,
    onCerrarSesionClick: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateActividades: () -> Unit // <-- 1. Nuevo parámetro
) {
    val palabras = nombreCompleto.trim().split(" ").filter { it.isNotEmpty() }
    val iniciales = if (palabras.size >= 2) {
        "${palabras[0].first()}${palabras[1].first()}".uppercase()
    } else if (palabras.size == 1) {
        palabras[0].take(2).uppercase()
    } else {
        "US"
    }

    Scaffold(
        bottomBar = {
            CapebeBottomNavigation(
                currentTab = "Perfil",
                onNavigateHome = onNavigateHome,
                onNavigateActividades = onNavigateActividades, // <-- 2. Conectado a la barra
                onNavigatePerfil = { }
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateHome,
                    modifier = Modifier.offset(x = (-12).dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF0C2E5C)
                    )
                }
                Text(
                    text = "Mi perfil",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C2E5C)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE8F1FA)).border(2.dp, Color(0xFFB0CDE6), CircleShape)) {
                Text(text = iniciales, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = nombreCompleto, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
            Text(text = correo, fontSize = 14.sp, color = Color(0xFF5A5A5A))
            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Información de la cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
                TextButton(onClick = onEditarClick, contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp), tint = Color(0xFF0C2E5C))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar", color = Color(0xFF0C2E5C), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column {
                    InfoRow(icon = Icons.Outlined.Person, label = "Nombre completo", value = nombreCompleto)
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    InfoRow(icon = Icons.Outlined.Email, label = "Correo electrónico", value = correo)
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    InfoRow(icon = Icons.Outlined.Badge, label = "RUT", value = rut)
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    InfoRow(icon = Icons.Outlined.People, label = "Categoría del participante", value = categoria)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(onClick = onCerrarSesionClick, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(26.dp), border = BorderStroke(1.dp, Color(0xFF0C2E5C))) {
                Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "Cerrar sesión", tint = Color(0xFF0C2E5C))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", color = Color(0xFF0C2E5C), fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFE8F1FA))) {
            Icon(icon, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = Color(0xFF5A5A5A))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
        }
    }
}