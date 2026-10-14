package com.example.capebe.ui.viewmodel




import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.capebe.data.local.entity.UsuarioEntity // <-- Ruta corregida

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