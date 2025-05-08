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
    private val user = FirebaseAuth.getInstance().currentUser

    var clases by mutableStateOf<List<ClaseConId>>(emptyList())
        private set

    init {
        val hoy = LocalDate.now()
        if (hoy.dayOfWeek == DayOfWeek.MONDAY) {
            resetearReservas()
        }
        cargarClases()
    }

    fun resetearReservas() {
        db.collection("clases").get().addOnSuccessListener { snapshot ->
            snapshot.documents.forEach { doc ->
                db.collection("clases").document(doc.id).update(
                    mapOf(
                        "usuarios" to emptyList<String>(),
                        "inscritos" to 0
                    )
                )
            }
        }
    }

    fun cargarClases() {
        db.collection("clases")
            .addSnapshotListener { result, error ->
                if (error != null) {
                    Log.e("Firestore", "🔥 Error general en snapshot", error)
                    return@addSnapshotListener
                }

                val lista = mutableListOf<ClaseConId>()

                result?.documents?.forEach { doc ->
                    try {
                        val clase = doc.toObject(Clase::class.java) ?: throw Exception("Clase vacía")
                        lista.add(ClaseConId(doc.id, clase))
                    } catch (e: Exception) {
                        Log.e("Firestore", "❌ Documento con error: ${doc.id} -> ${doc.data}", e)
                    }
                }

                clases = lista
            }
    }

    fun toggleReserva(claseId: String, clase: Clase) {
        val uid = user?.uid ?: return
        val nuevaLista = clase.usuarios.toMutableList()

        val yaReservado = nuevaLista.contains(uid)
        if (yaReservado) {
            nuevaLista.remove(uid)
        } else {
            if (clase.inscritos >= clase.capacidad) return
            nuevaLista.add(uid)
        }

        val actualizaciones = mapOf(
            "usuarios" to nuevaLista,
            "inscritos" to nuevaLista.size
        )

        db.collection("clases").document(claseId)
            .update(actualizaciones)
    }

    fun clasesReservadasPorUsuario(): List<ClaseConId> {
        val uid = user?.uid ?: return emptyList()
        return clases.filter { uid in it.clase.usuarios }
    }
}

