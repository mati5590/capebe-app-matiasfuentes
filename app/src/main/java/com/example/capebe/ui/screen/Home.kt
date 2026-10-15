package com.example.capebe.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    userName: String = "Valentina",
    onNavigateToPerfil: () -> Unit = {},
    onNavigateToActividades: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            CapebeBottomNavigation(
                currentTab = "Inicio",
                onNavigateHome = {},
                onNavigateActividades = onNavigateToActividades,
                onNavigatePerfil = onNavigateToPerfil
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            HeaderSection(userName, onNavigateToPerfil)
            Spacer(modifier = Modifier.height(24.dp))

            HeroBanner(onNavigateToActividades)

            Spacer(modifier = Modifier.height(32.dp))
            SectionHeader(title = "Próximas jornadas", actionText = "Ver todas", onActionClick = onNavigateToActividades)
            Spacer(modifier = Modifier.height(16.dp))
            JornadaCard()
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Mis inscripciones", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
            Spacer(modifier = Modifier.height(16.dp))
            EmptyInscripcionesCard()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HeaderSection(userName: String, onNavigateToPerfil: () -> Unit) {
    val iniciales = if (userName.length >= 2) userName.take(2).uppercase() else "VR"

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C2E5C))
            ) {
                Icon(Icons.Default.Flight, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "CAPEBE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0C2E5C), letterSpacing = 1.sp)
                Text(text = "¡Hola, $userName!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF222222))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = Color(0xFF0C2E5C))
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F1FA))
                    .border(1.dp, Color(0xFFB0CDE6), CircleShape)
                    .clickable { onNavigateToPerfil() }
            ) {
                Text(text = iniciales, color = Color(0xFF0C2E5C), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Prepárate para tu próxima experiencia de vuelo", fontSize = 14.sp, color = Color(0xFF5A5A5A))
}

@Composable
fun HeroBanner(onNavigateToActividades: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF09295A))
            .drawBehind {
                val circleCenter = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.5f)

                // Dibujamos solo los 3 círculos concéntricos con transparencia blanca
                drawCircle(
                    color = Color.White.copy(alpha = 0.03f),
                    radius = size.height * 1.1f,
                    center = circleCenter
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = size.height * 0.75f,
                    center = circleCenter
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = size.height * 0.4f,
                    center = circleCenter
                )

                // (La línea segmentada curva fue eliminada de aquí)
            }
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.65f)) {
            Text(text = "Vive la experiencia de volar", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Descubre las próximas jornadas de vuelos recreativos de CAPEBE", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onNavigateToActividades,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(text = "Ver vuelos disponibles", color = Color(0xFF0C2E5C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(16.dp))
            }
        }

        Icon(
            imageVector = Icons.Default.Flight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(75.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 10.dp)
        )
    }
}

@Composable
fun SectionHeader(title: String, actionText: String, onActionClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
        Text(text = actionText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0C2E5C), modifier = Modifier
            .padding(8.dp)
            .clickable { onActionClick() })
    }
}

@Composable
fun JornadaCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .width(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0C2E5C))
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("OCT", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("24", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    Text("SÁB", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Normal)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE5F7ED))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF198754), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Inscripciones abiertas", color = Color(0xFF198754), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Jornada de Primavera", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Santiago/Tobalaba", fontSize = 12.sp, color = Color(0xFF5A5A5A))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Vuelos recreativos de bienvenida para socios y beneficiarios. Cupos limitados.",
                fontSize = 13.sp,
                color = Color(0xFF5A5A5A),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2E5C)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text("Inscribirme", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }
                ) {
                    Text(text = "Ver detalles", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C2E5C))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyInscripcionesCard() {
    val strokeColor = Color(0xFFB0CDE6)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = strokeColor,
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    ),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .background(Color(0xFFF9FBFC), RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F1FA))
            ) {
                Icon(Icons.Outlined.Assignment, contentDescription = null, tint = Color(0xFF0C2E5C), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Aún no tienes inscripciones", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Aquí verás tu estado y tu horario.", fontSize = 12.sp, color = Color(0xFF5A5A5A), lineHeight = 16.sp)
            }
        }
    }
}

@Composable
fun CapebeBottomNavigation(
    currentTab: String,
    onNavigateHome: () -> Unit,
    onNavigateActividades: () -> Unit,
    onNavigatePerfil: () -> Unit
) {
    val darkBlue = Color(0xFF0C2E5C)
    val unselectedColor = Color(0xFF5A5A5A)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontWeight = if (currentTab == "Inicio") FontWeight.Bold else FontWeight.Normal, fontSize = 11.sp) },
            selected = currentTab == "Inicio",
            onClick = onNavigateHome,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = darkBlue,
                indicatorColor = darkBlue,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.CalendarToday, contentDescription = "Actividades") },
            label = { Text("Actividades", fontWeight = if (currentTab == "Actividades") FontWeight.Bold else FontWeight.Normal, fontSize = 11.sp) },
            selected = currentTab == "Actividades",
            onClick = onNavigateActividades,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = darkBlue,
                indicatorColor = darkBlue,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.FlightTakeoff, contentDescription = "Mis vuelos") },
            label = { Text("Mis vuelos", fontSize = 11.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontWeight = if (currentTab == "Perfil") FontWeight.Bold else FontWeight.Normal, fontSize = 11.sp) },
            selected = currentTab == "Perfil",
            onClick = onNavigatePerfil,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = darkBlue,
                indicatorColor = darkBlue,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor
            )
        )
    }
}