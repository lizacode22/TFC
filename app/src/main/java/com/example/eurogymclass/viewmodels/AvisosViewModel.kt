package com.example.eurogymclass.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Aviso(val fecha: String = "", val titulo: String = "", val descripcion: String = "")

class AvisosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    val avisos: StateFlow<List<Aviso>> = _avisos

    init {
        fetchAvisos()
    }

    private fun fetchAvisos() {
        db.collection("avisos")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    _avisos.value = listOf(
                        Aviso(
                            "Error",
                            "No se pudieron cargar los avisos",
                            "Intenta nuevamente más tarde"
                        )
                    )
                    return@addSnapshotListener
                }

                val lista = snapshot?.mapNotNull { document ->
                    val descripcion = document.getString("descripcion")
                    val fechaTimestamp = document.getTimestamp("fecha")
                    val fecha = fechaTimestamp?.toDate()?.toString() ?: ""
                    val titulo = document.getString("titulo")

                    if (!fecha.isNullOrEmpty() && !titulo.isNullOrEmpty() && !descripcion.isNullOrEmpty()) {
                        Aviso(titulo, descripcion, fecha)
                    } else {
                        null
                    }
                } ?: emptyList()

                _avisos.value = lista
            }

    }
}