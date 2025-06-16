package com.example.eurogymclass.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.DayOfWeek
import java.time.LocalDate

data class Clase(
    val titulo: String = "",
    val dia: String = "",
    val hora: String = "",
    val inscritos: Int = 0,
    val capacidad: Int = 0,
    val usuarios: List<String> = emptyList()
)

data class ClaseConId(
    val id: String,
    val clase: Clase
)

@RequiresApi(Build.VERSION_CODES.O)
class ClasesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usuarioActual = FirebaseAuth.getInstance().currentUser

    var clases by mutableStateOf<List<ClaseConId>>(emptyList())
        private set

    init {
        if (usuarioActual != null) {
            cargarClasesDesdeFirestore()
        }
    }

    fun cargarClasesDesdeFirestore() {
        if (usuarioActual == null) {
            Log.w("Firestore", "No hay usuario autenticado, no se pueden cargar clases.")
            return
        }

        db.collection("clases")
            .addSnapshotListener { result, error ->
                if (error != null) {
                    Log.e("Firestore", "Error general en snapshot", error)
                    return@addSnapshotListener
                }

                val clasesObtenidas  = mutableListOf<ClaseConId>()

                result?.documents?.forEach { doc ->
                    try {
                        val clase = doc.toObject(Clase::class.java) ?: throw Exception("Clase vacía")
                        clasesObtenidas.add(ClaseConId(doc.id, clase))
                    } catch (e: Exception) {
                        Log.e("Firestore", "Documento con error: ${doc.id} -> ${doc.data}", e)
                    }
                }

                clases = clasesObtenidas
            }
    }

    fun alternarReserva(claseId: String, clase: Clase) {

        val uid = usuarioActual?.uid ?: return
        val listaUsuarios = clase.usuarios.toMutableList()
        val yaReservado = uid in listaUsuarios

        if (yaReservado) {
            listaUsuarios.remove(uid)
        } else {
            if (clase.inscritos >= clase.capacidad) return
            listaUsuarios.add(uid)
        }

        val datosActualizados = mapOf(
            "usuarios" to listaUsuarios,
            "inscritos" to listaUsuarios.size
        )

        db.collection("clases").document(claseId)
            .update(datosActualizados)
            .addOnSuccessListener {
                val usuarioRef = db.collection("usuarios").document(uid)
                val actualizacionUsuario = if (yaReservado) {
                    com.google.firebase.firestore.FieldValue.arrayRemove(claseId)
                } else {
                    com.google.firebase.firestore.FieldValue.arrayUnion(claseId)
                }

                usuarioRef.update("clasesReservadas", actualizacionUsuario)
                    .addOnSuccessListener {
                        Log.i("Reserva", "Usuario actualizado con clase $claseId")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Reserva", "Error actualizando usuario: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Reserva", "Error actualizando clase: ${e.message}")
            }
    }


    fun clasesReservadasPorUsuario(): List<ClaseConId> {
        val uid = usuarioActual?.uid ?: return emptyList()
        return clases.filter { uid in it.clase.usuarios }
    }
}

