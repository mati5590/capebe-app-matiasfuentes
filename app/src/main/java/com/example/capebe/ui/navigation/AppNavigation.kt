package com.example.capebe.ui.navigation



import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.capebe.ui.screen.Actividades
import com.example.capebe.ui.screen.Home
import com.example.capebe.ui.screen.Login
import com.example.capebe.ui.screen.Perfil
import com.example.capebe.ui.screen.Register
import com.example.capebe.ui.viewmodel.LoginViewModel
import com.example.capebe.ui.viewmodel.RegisterViewModel
import com.example.capebe.ui.viewmodel.SessionViewModel

// Grafo principal de navegación de la aplicación (NavHost).
// Define las rutas y transiciones animadas entre pantallas, gestionando la instanciación
// de los ViewModels y el flujo de sesión. Se encarga de persistir de forma segura los datos
// del usuario en SharedPreferences al autenticarse y de purgar la caché local al cerrar sesión.
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = viewModel()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400)) }
    ) {

        composable("login") {
            val loginViewModel: LoginViewModel = viewModel()
            Login(
                viewModel = loginViewModel,
                onRegisterClick = { navController.navigate("registro") },
                onSuccess = { loggedUser ->
                    val sharedPref = context.getSharedPreferences("CAPEBE_PREFS", Context.MODE_PRIVATE)
                    sharedPref.edit().apply {
                        putInt("USER_ID", loggedUser.id)
                        putString("USER_NAME", loggedUser.nombreCompleto)
                        putString("USER_RUT", loggedUser.rut)
                        putString("USER_EMAIL", loggedUser.email)
                        putString("USER_CAT", loggedUser.categoria)
                        apply()
                    }

                    sessionViewModel.loginUser(loggedUser)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("registro") {
            val registerViewModel: RegisterViewModel = viewModel()
            Register(
                viewModel = registerViewModel,
                onLoginClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable("home") {
            val usuarioActual = sessionViewModel.currentUser
            val primerNombre = usuarioActual?.nombreCompleto?.split(" ")?.firstOrNull() ?: "Socio"

            Home(
                userName = primerNombre,
                onNavigateToPerfil = { navController.navigate("perfil") },
                onNavigateToActividades = { navController.navigate("actividades") }
            )
        }

        composable("actividades") {
            Actividades(
                onNavigateHome = { navController.navigate("home") { popUpTo(0) } },
                onNavigatePerfil = { navController.navigate("perfil") { popUpTo(0) } }
            )
        }

        composable("perfil") {
            val usuarioActual = sessionViewModel.currentUser

            Perfil(
                nombreCompleto = usuarioActual?.nombreCompleto ?: "Usuario Desconocido",
                correo = usuarioActual?.email ?: "Sin correo",
                rut = usuarioActual?.rut ?: "Sin RUT",
                categoria = usuarioActual?.categoria ?: "Socio",
                onEditarClick = { navController.navigate("editar_perfil") },
                onCerrarSesionClick = {
                    val sharedPref = context.getSharedPreferences("CAPEBE_PREFS", Context.MODE_PRIVATE)
                    sharedPref.edit().clear().apply()

                    sessionViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                onNavigateHome = { navController.navigateUp() },
                onNavigateActividades = { navController.navigate("actividades") }
            )
        }

        composable("editar_perfil") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pantalla de Editar Perfil en construcción")
            }
        }
    }
}