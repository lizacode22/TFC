package com.example.eurogymclass.viewmodels

import android.util.Log
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
        uid?.let { userId ->
            val userRef = db.collection("usuarios").document(userId)

            userRef.get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    usuario = doc.toObject(Usuario::class.java)
                } else {
                    // Documento no existe, crear uno nuevo con correo actual y nombre/apellidos vacíos
                    val correo = FirebaseAuth.getInstance().currentUser?.email ?: ""
                    val nuevoUsuario = Usuario(nombre = "", apellidos = "", email = correo)

                    userRef.set(nuevoUsuario).addOnSuccessListener {
                        usuario = nuevoUsuario
                        Log.i("Firestore", "Usuario creado correctamente.")
                    }.addOnFailureListener {
                        Log.e("Firestore", "Error al crear usuario", it)
                    }
                }
            }.addOnFailureListener {
                Log.e("Firestore", "Error al obtener usuario", it)
            }
        }
    }

    fun actualizarDatos(nombre: String, apellidos: String) {
        uid?.let {
            db.collection("usuarios").document(it)
                .update("nombre", nombre, "apellidos", apellidos)
                .addOnSuccessListener {
                    Log.i("Firestore", "Datos de usuario actualizados.")
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error actualizando datos de usuario", it)
                }
        }
    }
}