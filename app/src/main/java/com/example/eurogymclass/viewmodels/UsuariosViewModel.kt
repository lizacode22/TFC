package com.example.eurogymclass.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eurogymclass.data.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class UsuariosViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    init {
        sincronizarUsuario()
    }

    //Verificamos si el documento del usuario existe en Firestore. Si no existe, se creará con todos los campos.
    fun sincronizarUsuario() {
        uid?.let { userId ->
            val userRef = db.collection("usuarios").document(userId)

            userRef.get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    usuario = doc.toObject(Usuario::class.java)
                } else {
                    val firebaseUser = auth.currentUser
                    val correo = firebaseUser?.email ?: ""
                    val nombre = firebaseUser?.displayName ?: ""
                    val nuevoUsuario = Usuario(
                        nombre = nombre,
                        apellidos = "",
                        email = correo,
                        uid = userId,
                        fechaRegistro = Date(),
                        clasesReservadas = emptyList()
                    )

                    userRef.set(nuevoUsuario).addOnSuccessListener {
                        usuario = nuevoUsuario
                        Log.i("Firestore", "Usuario registrado correctamente.")
                    }.addOnFailureListener {
                        Log.e("Firestore", "Error al registrar usuario", it)
                    }
                }
            }.addOnFailureListener {
                Log.e("Firestore", "Error al obtener usuario", it)
            }
        }
    }

    //Actualizar el nombre y los apellidos del usuario en Firestore.
    fun actualizarDatos(nombre: String, apellidos: String) {
        if (nombre.isBlank() || apellidos.isBlank()) {
            Log.w("Firestore", "Los campos no pueden estar vacíos.")
            return
        }

        uid?.let {
            db.collection("usuarios").document(it)
                .update("nombre", nombre, "apellidos", apellidos)
                .addOnSuccessListener {
                    Log.i("Firestore", "Datos de usuario actualizados.")
                    usuario = usuario?.copy(nombre = nombre, apellidos = apellidos)
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error actualizando datos de usuario", it)
                }
        }
    }
}
