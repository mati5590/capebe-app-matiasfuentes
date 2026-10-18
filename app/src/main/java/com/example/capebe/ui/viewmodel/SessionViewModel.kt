package com.example.capebe.ui.viewmodel




import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.capebe.data.local.entity.UsuarioEntity

// ViewModel encargado de gestionar el estado global de la sesión del usuario en la aplicación.
// Mantiene en memoria la entidad del usuario actualmente autenticado de forma reactiva,
// permitiendo que las distintas pantallas compartan esta información y reaccionen a los
// eventos de inicio y cierre de sesión.
class SessionViewModel : ViewModel() {

    var currentUser by mutableStateOf<UsuarioEntity?>(null)
        private set

    fun loginUser(user: UsuarioEntity) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }
}