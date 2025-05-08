package com.example.eurogymclass.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eurogymclass.data.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UsuariosViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        uid?.let {
            db.collection("usuarios").document(it).get().addOnSuccessListener { doc ->
                usuario = doc.toObject(Usuario::class.java)
            }
        }
    }

    fun actualizarDatos(nombre: String, apellidos: String) {
        uid?.let {
            db.collection("usuarios").document(it)
                .update("nombre", nombre, "apellidos", apellidos)
        }
    }
}
