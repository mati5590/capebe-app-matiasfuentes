package com.example.capebe.ui.screen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Estructura de datos temporal para la UI
data class Jornada(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val ubicacion: String,
    val descripcion: String?,
    val estado: String // "Abiertas", "Cerrada", "Finalizada"
)

@Composable
fun Actividades(
    onNavigateHome: () -> Unit,
    onNavigatePerfil: () -> Unit
) {
    var filtroSeleccionado by remember { mutableStateOf("Todas") }
    val filtros = listOf("Todas", "Abiertas", "Cerradas", "Finalizadas")

    // Lista de jornadas basada en tu mockup
    val jornadas = listOf(
        Jornada(1, "Jornada de Primavera", "Sáb 24 oct 2026", "Santiago/Tobalaba", "Vuelos recreativos de bienvenida para socios y beneficiarios. Cupos limitados.", "Abiertas"),
        Jornada(2, "Jornada Costera", "Sáb 14 nov 2026", "Concepción/Talcahuano", "Jornada en la base de Talcahuano. El plazo de inscripción ya terminó.", "Cerrada"),
        Jornada(3, "Jornada de Invierno", "Sáb 12 sep 2026", "Santiago/Tobalaba", null, "Finalizada")
    )

    Scaffold(
        bottomBar = {
            CapebeBottomNavigation(
                currentTab = "Actividades",
                onNavigateHome = onNavigateHome,
                onNavigatePerfil = onNavigatePerfil,
                onNavigateActividades = { /* Ya estamos aquí */ }
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Título Superior
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = "Actividades", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0C2E5C))
                Text(text = "3 jornadas", fontSize = 14.sp, color = Color(0xFF5A5A5A), modifier = Modifier.padding(bottom = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros (Scroll horizontal)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filtros.forEach { filtro ->
                    FiltroChip(
                        texto = filtro,
                        seleccionado = filtroSeleccionado == filtro,
                        onClick = { filtroSeleccionado = filtro }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de Tarjetas
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                jornadas.forEach { jornada ->
                    JornadaItemCard(jornada)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FiltroChip(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (seleccionado) Color(0xFFE8F1FA) else Color.Transparent,
        border = BorderStroke(1.dp, if (seleccionado) Color.Transparent else Color(0xFFCCCCCC)),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (seleccionado) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = texto,
                color = if (seleccionado) Color(0xFF0C2E5C) else Color(0xFF222222),
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun JornadaItemCard(jornada: Jornada) {
    val (bgColor, textColor, icon, label) = when (jornada.estado) {
        "Abiertas" -> listOf(Color(0xFFE5F7ED), Color(0xFF198754), Icons.Outlined.CheckCircle, "Inscripciones abiertas")
        "Cerrada" -> listOf(Color(0xFFEEEEEE), Color(0xFF5A5A5A), Icons.Outlined.Lock, "Inscripción cerrada")
        else -> listOf(Color(0xFFEEEEEE), Color(0xFF5A5A5A), Icons.Outlined.Flag, "Jornada finalizada")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Etiqueta de Estado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(bgColor as Color).padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(icon as ImageVector, contentDescription = null, tint = textColor as Color, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label as String, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = jornada.titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
            Spacer(modifier = Modifier.height(8.dp))

            // Fecha y Ubicación
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Event, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = jornada.fecha, fontSize = 13.sp, color = Color(0xFF5A5A5A))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Outlined.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = jornada.ubicacion, fontSize = 13.sp, color = Color(0xFF5A5A5A))
            }

            // Descripción (Si existe)
            if (jornada.descripcion != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = jornada.descripcion, fontSize = 14.sp, color = Color(0xFF5A5A5A), lineHeight = 20.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (jornada.estado == "Abiertas") Arrangement.SpaceBetween else Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                if (jornada.estado == "Abiertas") {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2E5C)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Inscribirme", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                TextButton(onClick = { }) {
                    Text("Ver detalles >", color = Color(0xFF0C2E5C), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}